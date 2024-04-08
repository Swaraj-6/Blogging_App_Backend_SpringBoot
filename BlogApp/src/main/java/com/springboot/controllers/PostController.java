package com.springboot.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.config.AppConstants;
import com.springboot.payload.ApiResponse;
import com.springboot.payload.PostDto;
import com.springboot.payload.PostResponse;
import com.springboot.services.FileService;
import com.springboot.services.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		System.out.println(createPost);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}

	@GetMapping("/category/{catId}")
	public ResponseEntity<PostResponse> getPostbyCategory(@PathVariable Integer catId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORTING_TECHNIQUE, required = false) String sortBy) {
		PostResponse postResponse = this.postService.getPostsByCategory(catId, pageNumber, pageSize, sortBy);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<PostResponse> getPostByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORTING_TECHNIQUE, required = false) String sortBy) {
		PostResponse postResponse = this.postService.getPostsByUser(userId, pageNumber, pageSize, sortBy);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORTING_TECHNIQUE, required = false) String sortBy) {
		PostResponse postResponse = this.postService.getAllPosts(pageNumber, pageSize, sortBy);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/title/{title}")
	public ResponseEntity<PostResponse> getPostByTitle(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORTING_TECHNIQUE, required = false) String sortBy,
			@PathVariable String title) {

		PostResponse postResponse = this.postService.searchPosts(title, pageNumber, pageSize, sortBy);

		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Successfully.", true), HttpStatus.OK);
	}

	@PutMapping("/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto postDtoUpdated = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(postDtoUpdated, HttpStatus.OK);
	}

	// Post Images Upload

	@PostMapping("/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException {
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		
		postDto.setImageName(fileName);
		PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPostDto, HttpStatus.OK);
	}
	
	// Get Images
	
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException{
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
