package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * 搜索接口
 */
public interface ItemSearchService {
    //搜索
    public Map search(Map searchMap);

    //导入审核后的SKU商品
    public void importList(List list);

    //删除索引库数据
    public void deleList(List list);
}
