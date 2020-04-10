package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply;

public interface CommentMapper extends BaseMapper<Comment> {

	/**
	 * 查询所有评论和评论用户信息
	 * 
	 * @param writingId 文章id
	 * @return
	 */
	List<Comment> queryCommentsWithUser(String writingId);
	
	/**
	 * 查询指定回复及其用户信息
	 * 
	 * @param commentId 评论id
	 * @return
	 */
	List<Reply> queryRepliesWithUser(long commentId);
}
