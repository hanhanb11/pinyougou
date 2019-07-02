package han.dhll.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class MyMessageListener implements MessageListener {

    public void onMessage(Message message) {

        TextMessage textMessage=(TextMessage) message;

        try {
            System.out.println("提取信息："+textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
