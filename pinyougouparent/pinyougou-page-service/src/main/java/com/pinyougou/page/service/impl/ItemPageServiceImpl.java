package com.pinyougou.page.service.impl;


import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.serice.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;


    @Override
    public boolean getItemHtml(Long goodsId) {
        //创建工具对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //创建模板
        try {
            Template template = configuration.getTemplate("item.ftl");
            //创建数据模型
            Map dataModel = new HashMap();
            //1.商品主表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goods", goods);
            //2.商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goodsDesc", goodsDesc);
            //3.商品分类数据
            //一级分类名称
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            //二级分类名称
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            //三级分类名称
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();

            dataModel.put("itemCat1", itemCat1);
            dataModel.put("itemCat2", itemCat2);
            dataModel.put("itemCat3", itemCat3);

            //4.生成SKU（item列表数据）
            TbItemExample example =new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);//根据goodsId查询
            criteria.andStatusEqualTo("1");//根据状态查询
            example.setOrderByClause("is_default desc");//是否根据默认字段按照降序排序，目的就是返回第一条结果为默认SKU

            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList",itemList);


            //创建一个输出流
            Writer out = new FileWriter(pagedir + goodsId + ".html");
            //暑输出
            template.process(dataModel, out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


        return true;
    }


    @Override
    public boolean deleteHtml(Long[] goodsIds) {
        for (Long goodsId : goodsIds) {
            //遍历循环删除生成的静态页面
            new File(pagedir + goodsId + ".html").delete();
        }
        return true;
    }
}
