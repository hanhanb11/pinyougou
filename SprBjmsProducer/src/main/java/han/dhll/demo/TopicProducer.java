package han.dhll.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 发布/订阅模式（生产者）
 */
@Component
public class TopicProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicTextDestination;

    public void topicProducer(final String text){
        //使用框架
        jmsTemplate.send(topicTextDestination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                //创建消息生产者对象
                return session.createTextMessage(text);
            }
        });
    }




}
