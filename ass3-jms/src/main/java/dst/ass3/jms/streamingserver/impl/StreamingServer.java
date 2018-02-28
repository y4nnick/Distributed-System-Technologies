package dst.ass3.jms.streamingserver.impl;

import dst.ass3.dto.ClassifyEventWrapperDTO;
import dst.ass3.dto.EventWrapperDTO;
import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.jms.streamingserver.IStreamingServer;
import dst.ass3.model.LifecycleState;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by amra.
 */

@LocalClient
public class StreamingServer implements IStreamingServer, MessageListener {

    @Resource(mappedName = "connectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "streamingQueue")
    private Queue streamingQueue;

    @Resource(mappedName = "serverQueue")
    private Queue serverQueue;

    private MessageConsumer consumer;
    private MessageProducer producer;
    private Session session;

    private IStreamingServerListener listener;
    private String name;
    private  Connection connection;

    public StreamingServer(String name) {
        this.name=name;
    }


    @Override
    public void start() {
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(name);
            session= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(streamingQueue);
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
    public void setStreamingServerListener(IStreamingServerListener listener) {
        this.listener=listener;
    }


    @Override
    public void onMessage(Message message) {

        ObjectMessage m = (ObjectMessage) message;

        try {
            int messageType = message.getIntProperty(Scheduler.MESSAGE_IDENTIFIER);

            if (messageType == Scheduler.MESSAGE_SERVER_CREATED) {

                EventWrapperDTO eventWrapperDTO = (EventWrapperDTO) m.getObject();
                ClassifyEventWrapperDTO classifyEventWrapperDTO= new ClassifyEventWrapperDTO(eventWrapperDTO);
                IStreamingServerListener.EventWrapperDecideResponse response = listener.decideEvent(classifyEventWrapperDTO, name);

                eventWrapperDTO.setClassifiedBy(name);

                if (response.resp.equals(IStreamingServerListener.EventWrapperResponse.ACCEPT)) {
                    eventWrapperDTO.setType(response.type);
                    eventWrapperDTO.setState(LifecycleState.READY_FOR_STREAMING);
                } else {
                    eventWrapperDTO.setState(LifecycleState.STREAMING_NOT_POSSIBLE);
                }

                message = session.createObjectMessage(eventWrapperDTO);
                message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.UPDATE_MESSAGE);
                producer.send(message);

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
