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

@TableName(value = "reply")
public class Reply {

    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;
    private Long replyFromUserId;
    private Long replyToUserId;
    private Long replyCommentId;
    private String replyContent;
    private Date replyTime;
    @StaticURL
    @TableField(exist = false)
    private String replyFromAvatar;
    @TableField(exist = false)
    private String replyFromUserName;
    @TableField(exist = false)
    private String replyToUserName;

    @Override
    public String toString() {
        return "Reply [replyId=" + replyId + ", replyFromUserId=" + replyFromUserId + ", replyToUserId=" + replyToUserId
                + ", replyCommentId=" + replyCommentId + ", replyContent=" + replyContent + ", replyTime=" + replyTime
                + ", replyFromAvatar=" + replyFromAvatar + ", replyFromUserName=" + replyFromUserName
                + ", replyToUserName=" + replyToUserName + "]";
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyFromUserId() {
        return replyFromUserId;
    }

    public void setReplyFromUserId(Long replyFromUserId) {
        this.replyFromUserId = replyFromUserId;
    }

    public Long getReplyToUserId() {
        return replyToUserId;
    }

    public void setReplyToUserId(Long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyFromAvatar() {
        return replyFromAvatar;
    }

    public void setReplyFromAvatar(String replyFromAvatar) {
        this.replyFromAvatar = replyFromAvatar;
    }

    public String getReplyFromUserName() {
        return replyFromUserName;
    }

    public void setReplyFromUserName(String replyFromUserName) {
        this.replyFromUserName = replyFromUserName;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    public void setReplyToUserName(String replyToUserName) {
        this.replyToUserName = replyToUserName;
    }


}
