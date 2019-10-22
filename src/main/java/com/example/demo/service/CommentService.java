package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply;
import com.example.demo.exception.SystemException;

public interface CommentService extends BaseService<Comment>{

	/**
	 * 查询评论及回复
	 * 
	 * @param articeId 文章id
	 * @return
	 */
	List<Comment> queryCommentsWithRepies(long articeId);
	
	/**
	 * 新增回复
	 * 
	 * @param articeId 文章id
	 * @return
	 */
	int addOneReply(Reply reply) throws SystemException;
	
	/**
	 * 删除回复
	 * 
	 * @param articeId 文章id
	 * @return
	 */
	int deleteOneReply(long replyId) throws SystemException;
}
