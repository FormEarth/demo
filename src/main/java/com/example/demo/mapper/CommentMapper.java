package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.Comment;

public interface CommentMapper extends BaseMapper<Comment> {

	/**
	 * 查询评论及回复
	 * 
	 * @param articleId 文章id
	 * @return
	 */
	List<Comment> queryCommentsWithRepies(long articleId);
}
