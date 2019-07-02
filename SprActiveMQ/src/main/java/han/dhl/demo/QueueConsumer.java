package han.dhl.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 点对点（消费者）
 */
public class QueueConsumer {
    public static void main(String[] args) throws JMSException, IOException {

        //1.创建连接工程
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.25.153:61616");
        //2.创建连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.创建session会话对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Queue queue = session.createQueue("han-queue");
        //6.创建消息消费者为对象
        MessageConsumer consumer = session.createConsumer(queue);
        //7.设置监听
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("提取的消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //8.等待键盘输入
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
