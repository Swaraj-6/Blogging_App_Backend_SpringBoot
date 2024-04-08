package com.springboot.services;

import com.springboot.payload.PostDto;
import com.springboot.payload.PostResponse;

public interface PostService {

	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	
	public PostDto updatePost(PostDto postDto, Integer postId);
	
	public void deletePost(Integer postId);
	
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String SortBy);
	
	public PostDto getPostById(Integer postId);
	
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy);
	
	public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy);
	
	public PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy);

}
