package com.pinyougou.sellergoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Autowired
    private TbSellerMapper tbSellerMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }
    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");//状态：未审核
        goodsMapper.insert(goods.getTbGoods());//插入商品基本信息

        goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());//将商品基本表的ID给商品扩展类信息
        goodsDescMapper.insert(goods.getTbGoodsDesc());//插入商品扩展类信息

        saveItemList(goods);//插入SKU商品数据
    }

    //add方法提取公共部分进行封装方法
    private void setItemValues(TbItem item, Goods goods) {
        //2.存三级id
        Long category3Id = goods.getTbGoods().getCategory3Id();
        item.setCategoryid(category3Id);
        //3.存储创建时间
        item.setCreateTime(new Date());
        //4.存储更新时间
        item.setUpdateTime(new Date());
        //5.good-id存商品id
        item.setGoodsId(goods.getTbGoods().getId());
        //6.seller-id//存储卖家id
        item.setSellerId(goods.getTbGoods().getSellerId());
        //分类名称
        //7.存储商品名称
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(category3Id);
        item.setCategory(tbItemCat.getName());
        //8.存储brand名称
        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
        item.setBrand(tbBrand.getName());
        //9.存储商家名称（店铺名称）
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
        item.setSeller(tbSeller.getNickName());
        //10.存储图片
        List<Map> images = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);
        if (images.size() > 0) {
            item.setImage((String) images.get(0).get("url"));
        }
    }

    private  void saveItemList(Goods goods){
        //存储TbItem列表元素
        List<TbItem> itemList = goods.getItemList();
        if ("1".equals(goods.getTbGoods().getIsEnableSpec())) {
            //遍历
            for (TbItem item : itemList) {
                //1.储存title SPU名称 + 规格选型名称
                //三星 Galaxy A3 (A3009) 白色 电信4G手机 双卡双待
                String title = goods.getTbGoods().getGoodsName();
                Map<String, Object> map = JSON.parseObject(item.getSpec());
                for (String key : map.keySet()) {
                    title += " " + map.get(key);
                }
                item.setTitle(title);//存标题
                setItemValues(item, goods);//调用方法
                tbItemMapper.insert(item);
            }
        } else {
            TbItem item = new TbItem();
            item.setTitle(goods.getTbGoods().getGoodsName());//直接封装SKU名称
            item.setPrice(goods.getTbGoods().getPrice());//封装商品价格
            item.setNum(99999);//设置默认库存
            item.setStatus("1");//设置默认是否启用
            item.setIsDefault("1");//设置是否默认
            item.setSpec("{}");
            setItemValues(item, goods);//调用方法
            tbItemMapper.insert(item);
        }
    }
    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        //更新商品SPU基本信息tbGoods
        goodsMapper.updateByPrimaryKey(goods.getTbGoods());
        //更新商品SPU扩展信息tbGoodsDesc
        goodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());
        //首先删除商品SKU基本信息
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getTbGoods().getId());
        tbItemMapper.deleteByExample(example);
        //其次更新商品SKU基本信息
        saveItemList(goods);//插入SKU商品数据

    }


    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        //封装tbGoods
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setTbGoods(tbGoods);
        //封装tbGoodsDesc
        TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setTbGoodsDesc(goodsDesc);
        //商品SKU基本信息
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> items = tbItemMapper.selectByExample(example);
        goods.setItemList(items);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除数据库不删，只是修改状态
           // goodsMapper.deleteByPrimaryKey(id);
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("1");//表示逻辑删除，数据库还在
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        criteria.andIsDeleteIsNull();//筛选isDelete为null的字段
       // criteria.andIsMarketableIsNull();//筛选IsMarketable为null的字段


        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //查询商家后台商品信息，不能模糊查询，需要精确查询
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 商品审核和驳回
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        //遍历循环
        //System.out.println("status+......"+status);
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);//获取商品实体
            tbGoods.setAuditStatus(status);//设置状态
             goodsMapper.updateByPrimaryKey(tbGoods);//之后更新数据
        }
    }


    /**
     * 商家判断商品是否上下架
     * @param ids
     * @param isMarketable
     */
    @Override
    public void isMarketable(Long[] ids, String isMarketable) {
        System.out.println("isMarketable+..."+isMarketable);
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);//获取商品实体
            tbGoods.setAuditStatus(isMarketable);//设置状态
            goodsMapper.updateByPrimaryKey(tbGoods);//之后更新数据


        }
    }

}
