package com.example.demo.controller;

import com.example.demo.common.Dict;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.exception.SystemException;
import com.example.demo.service.TagService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author raining_heavily
 * @date 2019/11/3 15:48
 **/
@RestController
public class TagController {

    @Autowired
    TagService tagServiceImpl;

    @RequestMapping(value = "/tag",method = RequestMethod.POST)
    public JSONResult addNewTag(@RequestBody Tag tag) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        tag.setCreater(currentLoginUser.getUserId());
        tag.setTagStatus(Dict.TAG_STATUS_NORMAL);
        tag.setCreateTime(new Date());
        tagServiceImpl.add(tag);
        return JSONResult.success();
    }

    @RequestMapping(value = "/tag/{tagId}",method = RequestMethod.DELETE)
    public JSONResult deleteTagById(@PathVariable Tag tag) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        tag.setTagStatus(Dict.TAG_STATUS_DELETED);
        tag.setUpdater(currentLoginUser.getUserId());
        tagServiceImpl.updateById(tag);
        return JSONResult.success();
    }

    @RequestMapping(value = "/tags",method = RequestMethod.GET)
    public JSONResult queryAllTags() throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        return new JSONDataResult().add("tags",tagServiceImpl.queryAllTagsWithoutPage());
    }

    @RequestMapping(value = "/tags/search",method = RequestMethod.GET)
    public JSONResult queryTagsByKeyword(@RequestParam String searchText) throws SystemException {
        List<Tag> tagList;
        tagList = tagServiceImpl.queryTagsWithText(searchText);
        return new JSONDataResult().add("tags",tagList);
    }

    @RequestMapping(value = "/tag",method = RequestMethod.PUT)
    public JSONResult modifyTagById(@RequestBody Tag tag) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        tag.setUpdater(currentLoginUser.getUserId());
        tagServiceImpl.updateById(tag);
        return JSONResult.success();
    }

}
