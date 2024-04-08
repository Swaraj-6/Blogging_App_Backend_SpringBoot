package com.springboot.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.entity.Category;
import com.springboot.entity.Post;
import com.springboot.entity.User;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.payload.PostDto;
import com.springboot.payload.PostResponse;
import com.springboot.repositories.CategoryRepo;
import com.springboot.repositories.PostRepo;
import com.springboot.repositories.UserRepo;
import com.springboot.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		Optional<User> optionalUser = this.userRepo.findById(userId);
		Optional<Category> optionalCategory = this.categoryRepo.findById(categoryId);

		User user = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		} else {
			throw new ResourceNotFoundException("User", "User Id", userId);
		}
		Category category = null;
		if (optionalCategory.isPresent()) {
			category = optionalCategory.get();
		} else {
			throw new ResourceNotFoundException("Category", "Category Id", categoryId);
		}

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		
		Optional<Post> postOptional = this.postRepo.findById(postId);
		Post postUpdated = null;
		if(postOptional.isPresent()) {
			Post post = postOptional.get();
			post.setTitle(postDto.getTitle());
			post.setContent(postDto.getContent());
			post.setImageName(postDto.getImageName());
			
			postUpdated = this.postRepo.save(post);
		} else {
			throw new ResourceNotFoundException("Post", "Post Id", postId);
		}
		
		return this.modelMapper.map(postUpdated, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		
		Optional<Post> postOptional = this.postRepo.findById(postId);
		if (postOptional.isPresent()) {
			Post post = postOptional.get();
			this.postRepo.delete(post);
		} else {
			throw new ResourceNotFoundException("Post", "Post Id", postId);
		}
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		
		Page<Post> pagePosts = this.postRepo.findAll(pageable);
		List<Post> posts = pagePosts.getContent();
		
		List<PostDto> postDtos = new ArrayList<>();

		for (Post post : posts) {
			postDtos.add(this.modelMapper.map(post, PostDto.class));
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setFirstPage(pagePosts.isFirst());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {

		Optional<Post> postOptional = this.postRepo.findById(postId);
		Post post = null;
		if (postOptional.isPresent()) {
			post = postOptional.get();
		} else {
			throw new ResourceNotFoundException("Post", "Post Id", postId);
		}
		
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy) {

		Optional<Category> categoryOptional = this.categoryRepo.findById(categoryId);

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		Category category = null;
		if (categoryOptional.isPresent()) {
			category = categoryOptional.get();
		} else {
			throw new ResourceNotFoundException("Category", "Category Id", categoryId);
		}

		Page<Post> pagePosts = this.postRepo.findByCategory(category, pageable);
		List<Post> posts = pagePosts.getContent();
		List<PostDto> postDtos = new ArrayList<>();
		for (Post post : posts) {
			postDtos.add(this.modelMapper.map(post, PostDto.class));
		}
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setFirstPage(pagePosts.isFirst());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
	}

	@Override
	public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy) {

		Optional<User> userOptional = this.userRepo.findById(userId);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		User user = null;
		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			throw new ResourceNotFoundException("User", "User Id", userId);
		}

		Page<Post> pagePosts = this.postRepo.findByUser(user, pageable);
		List<Post> posts = pagePosts.getContent();
		List<PostDto> postDtos = new ArrayList<>();
		for (Post post : posts) {
			postDtos.add(this.modelMapper.map(post, PostDto.class));
		}

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setFirstPage(pagePosts.isFirst());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
	}

	@Override
	public PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy) {
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		Page<Post> pagePosts = this.postRepo.findByTitleContaining(keyword, pageable);

		if(pagePosts.isEmpty()) {
			throw new ResourceNotFoundException("Post", "Title", keyword);
		}
		List<Post> posts = pagePosts.getContent();
		List<PostDto> postDtos = new ArrayList<>();
		for (Post post : posts) {
			postDtos.add(this.modelMapper.map(post, PostDto.class));
		}
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setFirstPage(pagePosts.isFirst());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
	}

}
