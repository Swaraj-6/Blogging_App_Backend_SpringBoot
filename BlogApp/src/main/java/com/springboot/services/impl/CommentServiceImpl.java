package com.springboot.services.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.Comment;
import com.springboot.entity.Post;
import com.springboot.entity.User;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.payload.CommentDto;
import com.springboot.repositories.CommentRepo;
import com.springboot.repositories.PostRepo;
import com.springboot.repositories.UserRepo;
import com.springboot.services.CommentService;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
		Optional<User> optionalUser = this.userRepo.findById(userId);
		Optional<Post> optionalPost = this.postRepo.findById(postId);

		User user = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		} else {
			throw new ResourceNotFoundException("User", "User Id", userId);
		}
		Post post = null;
		if (optionalPost.isPresent()) {
			post = optionalPost.get();
		} else {
			throw new ResourceNotFoundException("Post", "Post Id", postId);
		}
		
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		
		Comment newComment = this.commentRepo.save(comment);
		
		return this.modelMapper.map(newComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Optional<Comment> commentOptional = this.commentRepo.findById(commentId);
		if (commentOptional.isPresent()) {
			Comment comment = commentOptional.get();
			this.commentRepo.delete(comment);
		} else {
			throw new ResourceNotFoundException("Comment", "Comment Id", commentId);
		}

	}

}
