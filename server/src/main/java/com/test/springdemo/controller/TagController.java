package com.test.springdemo.controller;

import com.test.springdemo.common.APIResponse;
import com.test.springdemo.common.ResponeCode;
import com.test.springdemo.entity.Tag;
import com.test.springdemo.permission.Access;
import com.test.springdemo.permission.AccessLevel;
import com.test.springdemo.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final static Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    TagService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse list(){
        List<Tag> list =  service.getTagList();

        return new APIResponse(ResponeCode.SUCCESS, "查询成功", list);
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public APIResponse create(Tag tag) throws IOException {
        service.createTag(tag);
        return new APIResponse(ResponeCode.SUCCESS, "创建成功");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public APIResponse delete(String ids){
        System.out.println("ids===" + ids);
        // 批量删除
        String[] arr = ids.split(",");
        for (String id : arr) {
            service.deleteTag(id);
        }
        return new APIResponse(ResponeCode.SUCCESS, "删除成功");
    }

    @Access(level = AccessLevel.ADMIN)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public APIResponse update(Tag tag) throws IOException {
        service.updateTag(tag);
        return new APIResponse(ResponeCode.SUCCESS, "更新成功");
    }

}