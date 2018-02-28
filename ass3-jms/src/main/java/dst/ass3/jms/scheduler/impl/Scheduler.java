package dst.ass3.jms.scheduler.impl;

import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by amra.
 */

@LocalClient
public class Scheduler implements IScheduler, MessageListener {

    public static final int ASSIGN_MESSAGE = 1;
    public static final int INFO_MESSAGE = 2;
    public static final int UPDATE_MESSAGE=10;

    public static final int MESSAGE_SERVER_CREATED = 3;
    public static final int MESSAGE_SERVER_INFO = 4;
    public static final int MESSAGE_SERVER_STREAMED = 5;
    public static final int MESSAGE_SERVER_DENIED = 6;
    public static final int MESSAGE_SERVER_FINISHED = 8;

    public static final String MESSAGE_IDENTIFIER = "msgidentifier";
    public static final int MESSAGE_UPLINK_FINISHED = 7;


    @Resource(mappedName = "connectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "schedulerQueue")
    private Queue schedulerQueue;

    @Resource(mappedName = "serverQueue")
    private Queue serverQueue;

    private MessageConsumer consumer;
    private MessageProducer producer;
    private Session session;
    private  Connection connection;
    private ISchedulerListener listener;


    @Override
    public void start() {

        try {
            connection = connectionFactory.createConnection();
            session= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(schedulerQueue);
            producer = session.createProducer(serverQueue);
            consumer.setMessageListener(this);
            connection.start();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (producer == null) producer.close();
            if (consumer == null) consumer.close();
            if (session == null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void assign(long eventId) {

        EventWrapperDTO eventWrapperDTO = new EventWrapperDTO();
        eventWrapperDTO.setEventId(eventId);
        try {
            ObjectMessage message = session.createObjectMessage(eventWrapperDTO);
            message.setIntProperty("msgidentifier", ASSIGN_MESSAGE);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void info(long eventWrapperId) {

        EventWrapperDTO eventWrapperDTO = new EventWrapperDTO();
        eventWrapperDTO.setId(eventWrapperId);
        try {
            ObjectMessage message = session.createObjectMessage(eventWrapperDTO);
            message.setIntProperty(MESSAGE_IDENTIFIER, INFO_MESSAGE);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSchedulerListener(ISchedulerListener listener) {
        this.listener=listener;

    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage m = (ObjectMessage)message;

        try {
            int messageType = message.getIntProperty(MESSAGE_IDENTIFIER);
            switch (messageType) {
                case MESSAGE_SERVER_CREATED:
                    listener.notify(ISchedulerListener.InfoType.CREATED, (EventWrapperDTO) m.getObject());
                    break;
                case MESSAGE_SERVER_INFO:
                    listener.notify(ISchedulerListener.InfoType.INFO, (EventWrapperDTO) m.getObject());
                    break;
                case MESSAGE_SERVER_FINISHED:
                    listener.notify(ISchedulerListener.InfoType.STREAMED, (EventWrapperDTO) m.getObject());
                    break;
                case MESSAGE_SERVER_DENIED:
                    listener.notify(ISchedulerListener.InfoType.DENIED, (EventWrapperDTO) m.getObject());
                    break;
                default: System.err.println("Unknown message: "+messageType);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
