package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务
 */
@Service(timeout = 5000)
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;//初始化solr

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        //关键字去空格
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));//去空格处理

        if (searchMap.size() > 0) {
            //1.查询列表
            map.putAll(searchList(searchMap));
            //2.分组查询
            List<String> categoryList = categoryList(searchMap);
            map.put("categoryList", categoryList);
            //3.根据模板ID获取品牌和规格列表
            String category = (String) searchMap.get("category");//获取商品分类
            if (!"".equals(category)) {//商品分类不为空，根据商品分类进行查询
                map.putAll(searchBrandAndSpecList(category));
            } else {
                if (categoryList.size() > 0) {
                    map.putAll(searchBrandAndSpecList(categoryList.get(0)));
                }
            }
        }
        return map;
    }

    //查询列表
    private Map searchList(Map searchMap) {
        Map map = new HashMap();

        //高亮搜索条件
        HighlightQuery query = new SimpleHighlightQuery();
        //<em style='color:red'>三星</em>
        HighlightOptions options = new HighlightOptions().addField("item_title");//设置高亮域
        options.setSimplePrefix("<em style='color:green'>");//设置前缀
        options.setSimplePostfix("</em>");//设置后缀
        query.setHighlightOptions(options);//设置高亮选项
        //1.1关键字查询keywords
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //1.2根据商品分类category查询
        if (!"".equals(searchMap.get("category"))) {//用户根据商品分类查询
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.3根据商品品牌brand查询
        if (!"".equals(searchMap.get("brand"))) {//用户选择品牌分类查询
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.4根据规格spec查询
        if (searchMap.get("spec") != null) {//如果规格列表不为空
            //spec:["":"","":""]
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));//动态域
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //#####################################################################################
        //1.5按照价格过滤500-1000
        if (!(searchMap.get("price").equals(""))) {
            String priceStr = (String) searchMap.get("price");//获取 如"0-500 "字符串
            String[] price = priceStr.split("-");//"-"分割

            if (Integer.parseInt(price[0]) > 0) {//如果最低价格大于等于0
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);//则大于条件
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!(price[1]).equals("*")) {//如果最高价格不等于*，则小于等于*
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);//则大于条件
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //#####################################################################################
        //1.6,分页
        Integer pageNo = (Integer) searchMap.get("pageNo");//指定当前页码
        if (pageNo == null) {
            pageNo = 1;//默认当前页为1
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");//每页记录数
        if (pageSize == null) {
            pageSize = 20;
        }
        query.setOffset((long) ((pageNo - 1) * pageSize));//计算每页从那条开始
        query.setRows(pageSize);
        //#####################################################################################
        //1.7排序
        String sortValue = (String) searchMap.get("sort");//获取排序
        String sortField = (String) searchMap.get("sortField");//获取排序字段
        System.out.println("sortValue:" + sortValue);
        System.out.println("sortField:" + sortField);
        if (sortValue != null && !sortValue.equals("")) {
            if (sortValue.equals("ASC")) {
                Sort sort = Sort.by(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);
            }
            if (sortValue.equals("DESC")) {

                Sort sort = Sort.by(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
//                //Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);//降序
//                query.addSort(sort);
            }
        }
        //#################################获取高亮结果集#####################################################
        //获取高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage("han", query, TbItem.class);
        //获取高亮入口
        for (HighlightEntry<TbItem> EntityList : page.getHighlighted()) {
            TbItem item = EntityList.getEntity();
            if (EntityList.getHighlights().size() > 0 && EntityList.getHighlights().get(0).getSnipplets().size() > 0) {
                item.setTitle(EntityList.getHighlights().get(0).getSnipplets().get(0));
            }
        }
        map.put("rows", page.getContent());
        map.put("totalPages", page.getTotalPages());//总页数
        //System.out.println("总页数：" + page.getTotalPages());
        map.put("total", page.getTotalElements());//总记录数
        //System.out.println("总记录数：" + page.getTotalElements());
        return map;
    }

    //分组查询
    private List<String> categoryList(Map searchMap) {
        //关键字查询
        Query query = new SimpleQuery("*:*");
        //关键字查询
        query.addCriteria(new Criteria("item_keywords").is(searchMap.get("keywords")));
        //设置分组选项
        /**
         * GroupOptions没有设置limit和offset的值导致的。
         * 返回到前面设置的地方去groupOptions.setXXX这两个值，
         * 发现是可以设置值的，设置上继续跑起来，发现没有问题了！
         */
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        groupOptions.setOffset(0);//设置起始位置
        groupOptions.setLimit(10);//设置每页显示条数
        query.setGroupOptions(groupOptions);
        //分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage("han", query, TbItem.class);
        //分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        //遍历分组集合
        List<String> list = new ArrayList<>();
        for (GroupEntry<TbItem> groupEntry : content) {
            String groupValue = groupEntry.getGroupValue();//分组的结果
            list.add(groupValue);//添加到集合中
        }
        //System.out.println("分组列表:..." + list);
        return list;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据模板ID获取品牌和规格列表
     * 在缓存中根据category获取模板ID
     *
     * @param category 商品分类名称
     * @return
     */
    private Map searchBrandAndSpecList(String category) {
        Map map = new HashMap();
        //根据category获取模板ID
        Long specId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        //System.out.println("模板Id:" + specId);
        //根据模板ID获取品牌和规格列表
        if (specId != null) {
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(specId);
            map.put("brandList", brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(specId);
            map.put("specList", specList);
        }
        return map;
    }

    /**
     * 导入审核后的sku
     * 列表
     *
     * @param list
     */
    @Override
    public void importList(List list) {
        solrTemplate.saveBeans("han", list);
        solrTemplate.commit("han");
    }

    /**
     * 更新索引库删除商品同步
     *
     * @param goodsid
     */
    @Override
    public void deleList(List goodsid) {
        SolrDataQuery query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_goodsid").in(goodsid);
        query.addCriteria(criteria);
        solrTemplate.delete("han", query);
        solrTemplate.commit("han");


    }


}
