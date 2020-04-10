package com.example.demo.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.entity.Comment;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.Reply;
import com.example.demo.entity.User;
import com.example.demo.exception.SystemException;
import com.example.demo.service.CommentService;

/**
 * @author raining_heavily
 */
@RestController
public class CommentController {

	@Autowired
	CommentService commentService;

	/**
	 * 根据作品Id获取评论
	 * @param writingId
	 * @return
	 */
	@StaticURL
	@RequestMapping(value = { "/{writingId}/comments" }, method = RequestMethod.GET)
	public JSONResult getCommentsByWritingId(@PathVariable String writingId){
		//如果允许评论，查询评论
		List<Comment> comments;
		comments = commentService.queryCommentsWithUser(writingId);
		return new JSONDataResult().add("comments", comments);
	}

	/**
	 * 添加评论
	 * @param comment
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/comment" }, method = RequestMethod.POST)
	public JSONResult addComment(@RequestBody Comment comment) throws SystemException {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		comment.setCommentTime(new Date());
		comment.setUserId(user.getUserId());
		commentService.add(comment);
		return new JSONDataResult().add("commentId", comment.getCommentId());
	}

	/**
	 * 删除评论
	 * @param commentId
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/comment" }, method = RequestMethod.DELETE)
	public JSONResult deleteComment(long commentId) throws SystemException {
		commentService.delete(new QueryWrapper<Comment>().eq("comment_id", commentId));
		return JSONResult.success();
	}
	
	/**
	 * 查询指定评论id下的所有回复
	 * @param commentId 评论id
	 * @return
	 * @throws SystemException 未查询到数据
	 */
	//TODO 分页
	@StaticURL
	@RequestMapping(value = { "/{commentId}/replies" }, method = RequestMethod.GET)
	public JSONResult getRepliesByCommentId(@PathVariable long commentId) throws SystemException {
		List<Reply> replies = commentService.queryRepliesWithUser(commentId);
		return new JSONDataResult().add("replies",replies );
	}

	/**
	 * 添加回复
	 * @param reply
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/reply" }, method = RequestMethod.POST)
	public JSONResult addOneReply(@RequestBody Reply reply) throws SystemException {
		reply.setReplyTime(new Date());
		commentService.addOneReply(reply);
		return new JSONDataResult().add("replyId", reply.getReplyId());
	}

	/**
	 * 删除回复
	 * @param replyId
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = { "/reply" }, method = RequestMethod.DELETE)
	public JSONResult deleteOneReply(long replyId) throws SystemException {
		commentService.deleteOneReply(replyId);
		return JSONResult.success();
	}
	
}
