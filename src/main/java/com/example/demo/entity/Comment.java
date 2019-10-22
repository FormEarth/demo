package com.example.demo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 
 * @author raining_heavily
 * @date 2019年10月16日
 * @time 下午7:51:14
 * @description 评论表（文章的评论）
 */
@TableName(value = "demo_comment")
public class Comment {
	
	@TableId(value = "comment_id", type = IdType.AUTO)
	long commentId;
	long userId;
	long articleId;
	String commentContent;
	Date commentTime;
	
	@TableField(exist=false)
	String avatar;
	@TableField(exist=false)
	String userName;
	/** 评论下面的回复 **/
	@TableField(exist=false)
	List<Reply> replies;
	
	public String getAvatar() {
		return "http://192.168.149.110:9090/static" + avatar;
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

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userId=" + userId + ", articeId=" + articleId + ", commentContent="
				+ commentContent + ", commentTime=" + commentTime + ", avatar=" + avatar + ", userName=" + userName
				+ ", replies=" + replies + "]";
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}


}
