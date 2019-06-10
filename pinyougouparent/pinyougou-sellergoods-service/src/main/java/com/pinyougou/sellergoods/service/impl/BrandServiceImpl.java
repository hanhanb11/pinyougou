package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    //Mybatis创建sqlSession 建立mapper
    @Autowired
    private TbBrandMapper brandMapper;
    /**
     * 查询所有品牌列表
     *
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        //传入null是查询所有
        return brandMapper.selectByExample(null);
    }
    /**
     * 品牌分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        //使用分页插件助手
        PageHelper.startPage(pageNum, pageSize);
        //得出page对象，传入null查询所有
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        //总共记录数
        long total = page.getTotal();
        //结果集
        List<TbBrand> result = page.getResult();
        //封装page所需对象
        return new PageResult(total, result);
    }
    /**
     * 增加实体类TbBrand
     *
     * @param brand
     */
    @Override
    public void addBrand(TbBrand brand) {
        brandMapper.insert(brand);
    }
    /**
     * 4.
     * ###########修改开始############
     * 第一步：4.1根据id先把TbBrand查询出来
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        //根据主键查询
        return brandMapper.selectByPrimaryKey(id);
    }
    /**
     * ############修改结束#########
     * 第二步：4.2修改之后再保存
     *
     * @param brand
     */
    @Override
    public void updateBrand(TbBrand brand) {
        //根据主键存储
        brandMapper.updateByPrimaryKey(brand);
    }
    /**
     * 5.根据id删除
     *
     * @param ids//复选删除
     */
    @Override
    public void delBrand(Long[] ids) {
        //遍历ids
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }
    /**
     * 6.品牌分页,品牌条件查询
     *
     * @param brand    品牌实体
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        //使用分页插件助手
        PageHelper.startPage(pageNum, pageSize);
        //获取条件
        TbBrandExample example = new TbBrandExample();
        //判断一下
        if(brand!=null){//判断集合不为空
            TbBrandExample.Criteria criteria = example.createCriteria();
            //判断输入的品牌名
            if(brand.getName()!=null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            //判断输入的品牌字母
            if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }
        //得出page对象，传入null查询所有
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        //总共记录数
        long total = page.getTotal();
        //结果集
        List<TbBrand> result = page.getResult();
        //封装page所需对象
        return new PageResult(total, result);
    }
}