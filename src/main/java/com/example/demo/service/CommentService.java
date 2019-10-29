package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply;
import com.example.demo.exception.SystemException;

public interface CommentService extends BaseService<Comment>{

	/**
	 * 查询评论
	 * 
	 * @param articeId 文章id
	 * @return
	 */
	List<Comment> queryCommentsWithUser(long articeId);
	
	/**
	 * 查询回复
	 * 
	 * @param commentId 评论id
	 * @return
	 * @throws SystemException 
	 */
	List<Reply> queryRepliesWithUser(long commentId) throws SystemException;
	
	/**
	 * 新增回复
	 * 
	 * @param reply 回复实体
	 * @return
	 */
	int addOneReply(Reply reply) throws SystemException;
	
	/**
	 * 删除回复
	 * 
	 * @param replyId 回复id
	 * @return
	 */
	int deleteOneReply(long replyId) throws SystemException;
}
