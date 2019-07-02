package com.pinyougou.page.service.impl;

import com.pinyougou.page.serice.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听类 发布订阅消费者
 */
@Component
public class PageListener implements MessageListener {

    //引入服务
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        //类型转换
        TextMessage textMessage = (TextMessage)message;

        try {
            String goodsId = textMessage.getText();

            System.out.println("提取消息："+ goodsId);

            boolean b = itemPageService.getItemHtml(Long.parseLong(goodsId));

            System.out.println("是否生成页面："+b);

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
