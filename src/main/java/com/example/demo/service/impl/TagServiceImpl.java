package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Dict;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.TagMapper;
import com.example.demo.service.TagService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author raining_heavily
 * @date 2019/11/3
 **/
@Service
public class TagServiceImpl extends BaseServiceImpl<Tag> implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public List<Tag> queryAllTagsWithoutPage() throws SystemException {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_status", Dict.TAG_STATUS_NORMAL);
        List<Tag> tagList;
        tagList = super.baseMapper.selectList(queryWrapper);
        if(tagList.size()<1){
            throw new SystemException(ExceptionEnums.DATA_SELECT_FAIL);
        }
        return tagList;
    }

    @Override
    public List<Tag> queryTagsWithText(String searchText) throws SystemException {
        return tagMapper.queryTagsWithText(searchText);
    }

    /**
     * 更新指定标签热度
     *
     * @param tagId
     * @return
     */
    @Override
    public int updateTagHotById(long tagId) throws SystemException {
        int count = tagMapper.updateTagHotById(tagId);
        if(count!=1){
            throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
        }
        return 1;
    }

    /**
     * 批量处理Tag，没有则新增，有则热度+1
     *
     * @param tags
     */
    @Transactional
    @Override
    public void handleTagList(List<Tag> tags) {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        for (Tag temp : tags) {
            if (-1 == temp.getTagId()) {
                //插入
                Tag tempTag = new Tag();
                tempTag.setTagText(temp.getTagText());
                tempTag.setCreater(currentLoginUser.getUserId());
                tempTag.setCreateTime(new Date());
                tempTag.setTagHot(1L);
                tagMapper.insert(tempTag);
                temp.setTagId(tempTag.getTagId());
            } else {
                //更新
                tagMapper.updateTagHotById(temp.getTagId());
            }
        }
    }


}
