package dst.ass3.jms;

import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.dto.StreamEventWrapperDTO;
import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.model.EventType;
import dst.ass3.model.EventWrapper;
import dst.ass3.model.LifecycleState;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by amra.
 */

@MessageDriven(mappedName = "serverQueue")
public class Server implements MessageListener{

    @Resource(mappedName = "connectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "streamingQueue")
    private Queue streamingQueue;

    @Resource(mappedName = "uplinkTopic")
    private Topic uplinkTopic;

    @Resource(mappedName = "schedulerQueue")
    private Queue schedulerQueue;

    @PersistenceContext
    private EntityManager entityManager;

    private Session session;
    private  Connection connection;

    private MessageProducer streamingServerMP;
    private MessageProducer uplinkMP;
    private MessageProducer schedulerMP;

    @PostConstruct
    public void initServer () {
        try {
            connection = connectionFactory.createConnection();
            session= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            streamingServerMP = session.createProducer(streamingQueue);
            uplinkMP = session.createProducer(uplinkTopic);
            schedulerMP = session.createProducer(schedulerQueue);

            connection.start();

        } catch (JMSException e) {
            System.out.println(e);
        }
    }

    @PreDestroy
    public void stopServer() {
        try {
            if (streamingServerMP != null) streamingServerMP.close();
            if (uplinkMP != null) uplinkMP.close();
            if (schedulerMP != null) schedulerMP.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println(e);
        }
    }


    @Override
    public void onMessage(Message message) {

        try {
            int messageType = message.getIntProperty(Scheduler.MESSAGE_IDENTIFIER);

            switch (messageType) {
                case Scheduler.ASSIGN_MESSAGE:
                    handleAssignMessage(message);
                    break;
                case Scheduler.INFO_MESSAGE:
                    handleInfoMessage(message);
                    break;
                case Scheduler.UPDATE_MESSAGE:
                    handleUpdateMessage(message);
                    break;
                case Scheduler.MESSAGE_UPLINK_FINISHED:
                    handleMessageUplinkFinished(message);
                    break;
                default:
                    System.err.println("Server: Unknown message: " + messageType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAssignMessage(Message message) throws Exception {

        ObjectMessage m = (ObjectMessage)message;
        EventWrapperDTO eventWrapperDTO = (EventWrapperDTO) m.getObject();

        if(eventWrapperDTO == null) throw new Exception("Null object in message");

        EventWrapper eventWrapper = new EventWrapper();
        eventWrapper.setEventId(eventWrapperDTO.getEventId());
        eventWrapper.setState(LifecycleState.ASSIGNED);
        eventWrapper.setType(EventType.UNCLASSIFIED);

        entityManager.persist(eventWrapper);

        message = session.createObjectMessage(new EventWrapperDTO(eventWrapper));
        message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_SERVER_CREATED);

        streamingServerMP.send(message);
        schedulerMP.send(message);
    }


    private void handleInfoMessage(Message message) throws Exception {

        ObjectMessage m = (ObjectMessage)message;
        EventWrapperDTO eventWrapperDTO = (EventWrapperDTO) m.getObject();

        if(eventWrapperDTO == null) throw new Exception("Null object in message");

        EventWrapper eventWrapper = entityManager.find(EventWrapper.class, eventWrapperDTO.getId());

        if(eventWrapper == null) throw new Exception("Eventwrapper is null");

        message = session.createObjectMessage(new EventWrapperDTO(eventWrapper));
        message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_SERVER_INFO);

        schedulerMP.send(message);
    }


    private void handleMessageUplinkFinished(Message message) throws Exception {

        ObjectMessage m = (ObjectMessage)message;
        StreamEventWrapperDTO streameventWrapperDTO = (StreamEventWrapperDTO) m.getObject();

        if(streameventWrapperDTO == null) throw new Exception("Null object in message");

        EventWrapper eventWrapper = entityManager.find(EventWrapper.class, streameventWrapperDTO.getId());
        if(eventWrapper == null) throw new Exception("Eventwrapper is null");

        eventWrapper.setState(LifecycleState.STREAMED);
        entityManager.persist(eventWrapper);

        message = session.createObjectMessage(new EventWrapperDTO(eventWrapper));
        message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_SERVER_FINISHED);

        schedulerMP.send(message);
    }

    private void handleUpdateMessage(Message message) throws Exception {

        ObjectMessage m = (ObjectMessage)message;
        EventWrapperDTO eventWrapperDTO = (EventWrapperDTO) m.getObject();

        if(eventWrapperDTO == null) throw new Exception("Null object in message");

        EventWrapper eventWrapper = entityManager.find(EventWrapper.class, eventWrapperDTO.getId());

        if(eventWrapper == null) throw new Exception("Null object in message (eventWrapper)");
        eventWrapper.setClassifiedBy(eventWrapperDTO.getClassifiedBy());
        eventWrapper.setState(eventWrapperDTO.getState());
        eventWrapper.setType(eventWrapperDTO.getType());

        entityManager.persist(eventWrapper);
        if (eventWrapper.getState() == null) throw new Exception();

        if (eventWrapperDTO.getState().equals(LifecycleState.READY_FOR_STREAMING)) {

            message = session.createObjectMessage(new StreamEventWrapperDTO(eventWrapperDTO));
            message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_SERVER_STREAMED);
            message.setStringProperty("streamingServerName", eventWrapper.getClassifiedBy());
            message.setStringProperty("eventType", eventWrapper.getType().toString());

            uplinkMP.send(message);

        } else if (eventWrapperDTO.getState().equals(LifecycleState.STREAMING_NOT_POSSIBLE)) {

            message = session.createObjectMessage(eventWrapperDTO);
            message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_SERVER_DENIED);

            schedulerMP.send(message);
        }
    }
}
