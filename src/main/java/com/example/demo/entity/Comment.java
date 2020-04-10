package com.example.demo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author raining_heavily
 * @date 2019年10月16日
 * @time 下午7:51:14
 * @description 评论表（文章的评论）
 */
@Data
@TableName(value = "demo_comment")
public class Comment {

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;
    private Long userId;
    private String writingId;
    private String commentContent;
    private Integer commentStatus;
    private Date commentTime;
    @StaticURL
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    /** 评论下面的回复数 **/
    private Long replyCount;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCommentTime() {
        return commentTime;
    }

}
