package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class contentController {

    @Reference
    private ContentService contentService;

    /**
     *
     * 查询内容条目Id
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId) {
       return contentService.findByCategoryId(categoryId);
    }


}
