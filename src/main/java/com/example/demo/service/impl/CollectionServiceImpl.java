package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Collection;
import com.example.demo.mapper.CollectionMapper;
import com.example.demo.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author raining_heavily
 * @date 2019/11/17 17:34
 **/
@Service
public class CollectionServiceImpl extends BaseServiceImpl<Collection> implements CollectionService{

    @Autowired
    CollectionMapper collectionMapper;

    @Override
    public List<Collection> queryAllCollectionsByUserId(Long userId){
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).orderByDesc("collect_time");
        return collectionMapper.selectList(queryWrapper);
    }
}
