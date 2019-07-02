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
 * 点对点模式（生产者）
 */
@Component
public class QueueProducer {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueTextDestination;

    /**
     * 发送文本信息
     * @param text
     */
    public void sentProducer(final String text){
        //使用jmsTemplate
        jmsTemplate.send(queueTextDestination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);
            }
        });
    }
}
