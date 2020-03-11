package com.example.demo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author raining_heavily
 * @date 2019年10月16日
 * @time 下午7:51:14
 * @description 评论表（文章的评论）
 */
@TableName(value = "demo_comment")
public class Comment {

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;
    private Long userId;
    private Long articleId;
    private String commentContent;
    private Date commentTime;
    /** 楼数 **/
    private Integer commentIndex;
    @StaticURL
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    /** 评论下面的回复数 **/
    private Long replyCount;

    @Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userId=" + userId + ", articleId=" + articleId
				+ ", commentContent=" + commentContent + ", commentTime=" + commentTime + ", commentIndex="
				+ commentIndex + ", avatar=" + avatar + ", userName=" + userName + ", replyCount=" + replyCount + "]";
	}

	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Long replyCount) {
        this.replyCount = replyCount;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

	public Integer getCommentIndex() {
		return commentIndex;
	}

	public void setCommentIndex(Integer commentIndex) {
		this.commentIndex = commentIndex;
	}
    
    

}
