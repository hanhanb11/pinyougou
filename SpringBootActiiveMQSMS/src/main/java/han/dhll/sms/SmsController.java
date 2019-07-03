package han.dhll.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
/*
开发者
 */
@RestController
public class SmsController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    //发送MaP
    @RequestMapping("/sendMap")
    public void sendMap(){
        Map map =new HashMap();
       map.put("mobile","16621300728");
       map.put("templateCode","SMS_169635307");
       map.put("signName","流浪者");
       map.put("templateParam","{\"number\":\"12345\"}");

        jmsMessagingTemplate.convertAndSend("sms",map);
    }



}
