package com.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
