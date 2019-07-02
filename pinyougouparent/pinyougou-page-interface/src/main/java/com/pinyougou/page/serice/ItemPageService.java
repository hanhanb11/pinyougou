package com.pinyougou.page.serice;

//商品详细页接口
public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodsId
     * @return
     */
    public  boolean getItemHtml(Long goodsId);

    public  boolean deleteHtml(Long[] goodsIds);
}
