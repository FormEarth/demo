package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户关注、收藏、喜欢<br>（实际上不建议这样，相当于三个表合为一个了）
 * @author raining_heavily
 * @date 2019/11/17 16:33
 **/
@Data
@TableName("demo_collection")
public class Collection {
    /** 主键 **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id **/
    private Long userId;
    /** 用户动作 **/
    private Integer collectionType;
    /** 动作对象 **/
    private String collectionId;
    /** 时间 **/
    private Date collectTime;
}
