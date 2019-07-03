package han.dhll.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听类
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @JmsListener(destination = "sms_text")
    //消费者
    public void acceptSms(Map<String, String> map) {

        try {
            SendSmsResponse sendSms =
                    smsUtil.sendSms(
                            map.get("mobile"),
                            map.get("signName"),
                            map.get("templateCode"),
                            map.get("templateParam"));


            System.out.println("code:" + sendSms.getCode());
            System.out.println("message:" + sendSms.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


}
