package com.pinyougou.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    //引入点对点队列
    @Autowired
    private Destination queueDeleteSolrDestination;//点对点删除solr索引库
    @Autowired
    private  Destination topicDeletePageDestination;//发布订阅删除静态页面

    @RequestMapping("/delete")
    public Result delete(final Long[] ids) {
        try {
            goodsService.delete(ids);

            //1.通过消息中间件ActiveMQ实现在商品删除时也同时移除solr索引库记录的功能。
            jmsTemplate.send(queueDeleteSolrDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    //使用创建对象
                    return session.createObjectMessage(ids);
                }
            });

            //2.删除商品时删除生成的静态页
            jmsTemplate.send(topicDeletePageDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    //使用创建对象
                    return session.createObjectMessage(ids);
                }
            });
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * 删除只是修改状态不操作数据库
     * + 商品审核
     *
     * @param ids
     * @param status
     * @return
     */
    @Autowired
   private JmsTemplate jmsTemplate;
    @Autowired
   private Destination queueSolrDestination;//用户生产solr服务（点对点模式）
    @Autowired
    private  Destination topicPageDestination;//用于生成静态页面服务（发布订阅模式）

    @RequestMapping("/updateStatus")
    public Result updateStatus(final Long[] ids, String status) {
        //商品审核成功后跳转到消息中间站，ActiveMQ
        try {
            goodsService.updateStatus(ids, status);
            //1.################
            if ("1".equals(status)) {//如果审核通过
                final List<TbItem> itemList = goodsService.findItemListByGoodSIdsAndStatus(ids, status);
                //itemSearchService.importList(itemList);//导入solr
                //JMS 消息生产者
                jmsTemplate.send(queueSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        //将itemList转换成JSONZ字符串
                        String jsonString = JSON.toJSONString(itemList);
                        return   session.createTextMessage(jsonString);
                    }
                });
            }
           //2.###############生成商品详情页
            for (final Long goodsId:ids) {
                //itemPageService.getItemHtml(goodsId);
                jmsTemplate.send(topicPageDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(goodsId+"");
                    }
                });
            }
            return new Result(true, "状态修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "状态修改失败");
        }

    }

//    /**
//     * freeMarker静态生成页面
//     * @param goodsId
//     */
//    @RequestMapping("/getHtml")
//    public void getHtml(Long goodsId){
//       // itemPageService.getItemHtml(goodsId);
//    }

}
