package com.example.demo.controller;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
