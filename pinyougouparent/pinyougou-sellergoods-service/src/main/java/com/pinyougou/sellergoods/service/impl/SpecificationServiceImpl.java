package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Specification specification) {
        //获取规格名称实体
        TbSpecification tbSpecification = specification.getSpecification();
        specificationMapper.insert(tbSpecification);
        Long id = tbSpecification.getId();
        //获取新增规则选项list
        List<TbSpecificationOption> specOptionList = specification.getSpecificationOptionList();
        //遍历list
        for (TbSpecificationOption SpecOption : specOptionList) {
            SpecOption.setSpecId(tbSpecification.getId());//设置规格ID
            specificationOptionMapper.insert(SpecOption);//新增规格
        }
    }

    /**
     * 修改
     */
    @Override
    public void update(Specification specification) {
        //获取规格实体
        TbSpecification tbspec = specification.getSpecification();
        specificationMapper.updateByPrimaryKey(tbspec);
		//删除原来规格对应的规格选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(tbspec.getId());
        specificationOptionMapper.deleteByExample(example);

        //获取规格选项集合
        List<TbSpecificationOption> specOption = specification.getSpecificationOptionList();
        for (TbSpecificationOption option : specOption) {
            option.setSpecId(tbspec.getId());
            specificationOptionMapper.insert(option);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        //创建实体规格对象
        Specification spec = new Specification();
        //1获取tbSpecification规格实体
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        //1.1封装实体对象
        spec.setSpecification(tbSpecification);
        //2.获取规格选项
        //创建条件example
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        spec.setSpecificationOptionList(specificationOptionMapper.selectByExample(example));

        return spec;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {

        for (Long id : ids) {
            //删除规格实体
            specificationMapper.deleteByPrimaryKey(id);

            //删除规格选项
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            //把TbSpecification表中id赋予TbSpecificationOption，
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
        }
    }


    @Override
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
        //获取组合实体对象
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        TbSpecificationExample example = new TbSpecificationExample();
        Criteria criteria = example.createCriteria();
        if (specification != null) {
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
