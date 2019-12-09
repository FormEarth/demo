package com.example.demo.service;

import com.example.demo.entity.Collection;

import java.util.List;

/**
 * @author raining_heavily
 * @date 2019/11/17 17:33
 **/
public interface CollectionService extends BaseService<Collection>{

    /**
     * 根据用户Id查询用户关注、喜欢、收藏信息
     * @param userId
     * @return
     */
    public List<Collection> queryAllCollectionsByUserId(Long userId);

}
