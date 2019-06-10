package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

public interface BrandService {

    /**
     * 1.品牌接口
     * @return
     */
    public List<TbBrand> findAll();

    /**
     * 2.品牌分页
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     * @return
     */
    public PageResult findPage(int pageNum,int pageSize);

    /**
     * 3.增加品牌实体类
     * @param brand
     */
    public void addBrand(TbBrand brand);

    /**
     * 4.
     * ###########修改开始############
     * 第一步：4.1根据id先把TbBrand查询出来
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * ############修改结束#########
     *第二步：4.2修改之后再保存
     * @param brand
     */
    public void updateBrand(TbBrand brand);

    /**
     * 5.根据id删除
     * @param ids//复选删除
     */
    public void delBrand(Long[] ids);

    /**
     * 6.品牌分页,品牌条件查询
     * @param brand  品牌实体
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     * @return
     */
    public PageResult findPage(TbBrand brand,int pageNum,int pageSize);

}
