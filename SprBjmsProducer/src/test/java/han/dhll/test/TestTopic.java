package han.dhll.test;

import han.dhll.demo.TopicProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-jms-producer.xml")
public class TestTopic {

    @Autowired
    private TopicProducer topicProducer;

    /**
     * 发布/订阅模式，（生产者）
     */
    @Test
    public void testTopicProducer(){
        topicProducer.topicProducer("hello,topic 发布订阅...");
    }
}
