package dst.ass3.jms.uplink.impl;

import dst.ass3.dto.StreamEventWrapperDTO;
import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.jms.uplink.IUplink;
import dst.ass3.model.EventType;
import dst.ass3.model.LifecycleState;
import org.apache.openejb.api.LocalClient;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by amra.
 */
@LocalClient
public class Uplink implements IUplink, MessageListener {

    @Resource(mappedName = "connectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "serverQueue")
    private Queue serverQueue;

    @Resource(mappedName = "uplinkTopic")
    private Topic topic;

    private MessageConsumer consumer;
    private MessageProducer producer;
    private Session session;

    private IUplinkListener listener;
    private String name;
    private String streamingServer;
    private EventType type;
    private  Connection connection;

    public Uplink(String name, String streamingServer, EventType type) {
        this.name=name;
        this.streamingServer=streamingServer;
        this.type=type;
    }
    @Override
    public void start() {

        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(name);
            session= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(serverQueue);

            String messageFilter = String.format("streamingServerName ='%s' AND eventType ='%s'", streamingServer, type);

            consumer = session.createDurableSubscriber(topic, name, messageFilter, false);
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
    public void setUplinkListener(IUplinkListener listener) {
        this.listener=listener;
    }

    @Override
    public void onMessage(Message message) {

        ObjectMessage m = (ObjectMessage) message;

        try {
            int messageType = message.getIntProperty(Scheduler.MESSAGE_IDENTIFIER);

            if (messageType == Scheduler.MESSAGE_SERVER_STREAMED) {

                StreamEventWrapperDTO streamEventWrapperDTO = (StreamEventWrapperDTO) m.getObject();
                if (listener == null) throw new Exception("uplink listener is null");
                listener.waitTillStreamed(streamEventWrapperDTO,name,type,streamingServer);
                streamEventWrapperDTO.setState(LifecycleState.STREAMED);


                message = session.createObjectMessage(streamEventWrapperDTO);
                message.setIntProperty(Scheduler.MESSAGE_IDENTIFIER, Scheduler.MESSAGE_UPLINK_FINISHED);

                producer.send(message);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
