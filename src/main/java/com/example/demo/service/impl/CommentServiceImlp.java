package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.ReplyMapper;
import com.example.demo.service.CommentService;

@Service
public class CommentServiceImlp extends BaseServiceImpl<Comment> implements CommentService {

	@Autowired
	CommentMapper commentMapper;
	@Autowired
	ReplyMapper replyMapper;
	
	@Override
	public List<Comment> queryCommentsWithUser(long articeId) {
		List<Comment> comments = new ArrayList<>();
		comments = commentMapper.queryCommentsWithUser(articeId);
		return comments;
	}

	@Override
	public int addOneReply(Reply reply) throws SystemException {
		int i = 0;
		i = replyMapper.insert(reply);
		if(i < 1) throw new SystemException(ExceptionEnums.DATA_INSERT_FAIL);
		return i;
	}

	@Override
	public int deleteOneReply(long replyId) {
		int i = 0;
		i = replyMapper.deleteById(replyId);
		return i;
	}

	@Override
	public List<Reply> queryRepliesWithUser(long commentId) throws SystemException {
		List<Reply> replies = new ArrayList<>();
		replies = commentMapper.queryRepliesWithUser(commentId);
		if(replies.size()<1) {
			throw new SystemException(ExceptionEnums.SELECT_DATA_IS_EMPTY);
		}
		return replies;
	}
	
	

}
