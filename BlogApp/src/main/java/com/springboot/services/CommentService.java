package com.springboot.services;

import com.springboot.payload.CommentDto;

public interface CommentService {

	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
	
	public void deleteComment(Integer commentId);
}
