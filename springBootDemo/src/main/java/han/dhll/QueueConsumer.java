package han.dhll;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueueConsumer {


    @JmsListener(destination ="han")
    public void consumerQueue(String text){

        System.out.println("接受 消息:"+text);

    }

    @JmsListener(destination ="han-map")
    public void consumerQueue(Map map){

        System.out.println("接受 消息:"+map);

    }
}
