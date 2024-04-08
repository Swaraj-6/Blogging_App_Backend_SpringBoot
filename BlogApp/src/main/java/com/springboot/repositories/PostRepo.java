package com.springboot.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Category;
import com.springboot.entity.Post;
import com.springboot.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer>{

	Page<Post> findByUser(User user, Pageable pageable);
	
	Page<Post> findByCategory(Category category, Pageable pageable);
	
	Page<Post> findByTitleContaining(String title, Pageable pageable);
}
