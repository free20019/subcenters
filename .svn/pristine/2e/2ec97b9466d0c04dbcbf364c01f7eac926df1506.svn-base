package mvc.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;

@Service
public class ProducerService {

    @Resource(name = "jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Resource(name = "queueDestination")
    private Destination responseDestination;

    public void sendMessage(final String msg){
        System.out.println(">>>>\n"+Thread.currentThread().getName()+" 向队列"+responseDestination+"发送消息---------------------->"+msg);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(msg);
                textMessage.setJMSReplyTo(responseDestination);
                return textMessage;
            }
        });
    }
}