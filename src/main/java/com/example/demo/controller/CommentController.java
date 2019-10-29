package com.example.demo.controller;


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

@RestController
@RequestMapping(value = "/demo/api")
public class CommentController {

	@Autowired
	CommentService commentService;

	@RequestMapping(value = { "/comment" }, method = RequestMethod.POST)
	public JSONResult addComment(@RequestBody Comment comment) throws SystemException {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		comment.setCommentTime(new Date());
		comment.setUserId(user.getUserId());
		commentService.add(comment);	
		return new JSONDataResult().add("commentId", comment.getCommentId());
	}
	
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
	
	@RequestMapping(value = { "/reply" }, method = RequestMethod.POST)
	public JSONResult addOneReply(@RequestBody Reply reply) throws SystemException {
		reply.setReplyTime(new Date());
		commentService.addOneReply(reply);
		return new JSONDataResult().add("replyId", reply.getReplyId());
	}
	
	@RequestMapping(value = { "/reply" }, method = RequestMethod.DELETE)
	public JSONResult deleteOneReply(long replyId) throws SystemException {
		commentService.deleteOneReply(replyId);
		return JSONResult.success();
	}
	
}
