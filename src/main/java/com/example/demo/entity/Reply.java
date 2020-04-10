package com.example.demo.entity;
/**
 * @author raining_heavily
 * @date 2019年10月16日
 * @time 下午7:52:09
 * @description 回复表（针对评论的评论）
 */

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName(value = "demo_reply")
public class Reply {

    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;
    private Long replyFromUserId;
    private Long replyToUserId;
    private Long replyCommentId;
    private String replyContent;
    private Integer replyStatus;
    private Date replyTime;
    @StaticURL
    @TableField(exist = false)
    private String replyFromAvatar;
    @TableField(exist = false)
    private String replyFromUserName;
    @TableField(exist = false)
    private String replyToUserName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public void getReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

}
