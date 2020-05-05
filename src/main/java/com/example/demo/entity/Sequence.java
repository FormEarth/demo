package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author raining_heavily
 * @date 2020/4/18 17:05
 **/
@Data
@TableName(value = "demo_sequence")
public class Sequence {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String sequenceType;
    private Long sequenceNo;
    private Date lastUpdateTime;
}
