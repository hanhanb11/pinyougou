package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 1.返回品牌列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 2.使用分页助手实现品牌分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int pageNum, int pageSize) {
        return brandService.findPage(pageNum, pageSize);
    }

    /**
     * 3.添加实体类
     *
     * @return
     */
    @RequestMapping("/addBrand")
    public Result addBrand(@RequestBody TbBrand brand) {
        try {
            brandService.addBrand(brand);//添加
            return new Result(true, "保存成功...");   //判断
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败...");
        }

    }

    /**
     * 4.
     * ###########修改开始############
     * 第一步：根据id先把TbBrand查询出来
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }
    /**
     * ############修改结束#########
     *第二步：4.2修改之后再保存
     * @param brand
     */
    @RequestMapping("/updateBrand")
    public Result updateBrand(@RequestBody TbBrand brand){
        try {
            brandService.updateBrand(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
    /**
     * 5.根据id删除
     * @param ids//复选删除
     */
    @RequestMapping("/delBrand")
    public Result delBrand(Long[] ids){
        try {
            brandService.delBrand(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
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
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand brand,int pageNum, int pageSize){
        return brandService.findPage(brand,pageNum,pageSize);
    }

    /**
     * 7.下拉品牌列表
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }
}


