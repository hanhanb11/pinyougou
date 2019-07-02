package com.pinyougou.page.service.impl;


import com.pinyougou.page.serice.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * 监听类，监听消息中间件，批量删除生成的静态页面
 */
@Component
public class PageDeleteListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        //获取消息对象
        ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("提取消息中间件信息："+goodsIds[0]);
            boolean b = itemPageService.deleteHtml(goodsIds);
            System.out.println("页面删除结果："+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
