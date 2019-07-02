package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 删除善品是从索引库删除数据，消费者，监听类
 */
@Component
public class itemDeleteSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {
       ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("提取信息"+ids);
            itemSearchService.deleList(Arrays.asList(ids));
            System.out.println("从索引库删除");
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
