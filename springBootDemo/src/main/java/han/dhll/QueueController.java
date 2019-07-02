package han.dhll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QueueController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    //发送文本
    @RequestMapping("/send")
    public void send(String text){
    jmsMessagingTemplate.convertAndSend("han",text);
    }
    //发送MaP
    @RequestMapping("/sendMap")
    public void sendMap(){
        Map map =new HashMap();
       map.put("han","你好");
       map.put("hhh","jjjj");
        jmsMessagingTemplate.convertAndSend("han-map",map);
    }



}
