package mvc.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Service
public class ConsumerService {

    @Resource(name = "jmsQueueTemplate1")
    private JmsTemplate jmsTemplate;

    public String receive(Destination destination){
        TextMessage textMessage = (TextMessage) jmsTemplate.receive(destination);
        String info = "";
        try{
        	info =  textMessage.getText();
            System.out.println("从队列" + destination.toString() + "收到了消息：\t"
                    + info);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return info;
    }
}