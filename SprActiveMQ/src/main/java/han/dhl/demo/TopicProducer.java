package han.dhl.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 发布/订阅模式（生产者）
 */
public class TopicProducer {
    public static void main(String[] args) throws JMSException {

        //1.创建工厂连接
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.25.153:61616");
        //2.创建连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.创建session会话对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Topic topic = session.createTopic("han-topic");
        //6.创建消息生产者对象
        MessageProducer producer = session.createProducer(topic);
        //7.创建消息对象
        TextMessage textMessage = session.createTextMessage("欢迎来到神器的ActiveMq世界");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();




    }
}
