package han.dhll.test;

import han.dhll.demo.QueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-jms-producer.xml")
public class TestQueue {

    @Autowired
    private QueueProducer queueProducer;

    /**
     * 点对点模式。消费者
     */
    @Test
    public void testSend() {
        queueProducer.sentProducer("点对点，Queue-----------");
    }


}
