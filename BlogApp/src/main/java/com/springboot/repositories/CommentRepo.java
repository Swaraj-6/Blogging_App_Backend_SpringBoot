package com.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

}
