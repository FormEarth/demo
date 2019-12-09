package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.common.CollectionTypeEnum;

import java.util.Date;

/**
 * 用户关注、收藏、喜欢<br>（实际上不建议这样，相当于三个表合为一个了）
 * @author raining_heavily
 * @date 2019/11/17 16:33
 **/
@TableName("demo_collection")
public class Collection {
    /** 用户id **/
    private Long userId;
    /** 用户动作 **/
    private CollectionTypeEnum collectionType;
    /** 动作对象 **/
    private String collectionId;
    /** 时间 **/
    private Date collectTime;

    @Override
    public String toString() {
        return "Collection{" +
                "userId=" + userId +
                ", collectionType='" + collectionType + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", collectTime=" + collectTime +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CollectionTypeEnum getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionTypeEnum collectionType) {
        this.collectionType = collectionType;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
