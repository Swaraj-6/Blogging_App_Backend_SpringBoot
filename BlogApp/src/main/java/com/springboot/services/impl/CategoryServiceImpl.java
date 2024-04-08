package com.springboot.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.Category;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.payload.CategoryDto;
import com.springboot.repositories.CategoryRepo;
import com.springboot.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category addedCategory = this.categoryRepo.save(category);
		
		return this.modelMapper.map(addedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Optional<Category> categoryOptional = this.categoryRepo.findById(categoryId);
		Category categoryReturn = null;
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			
			category.setCategoryTitle(categoryDto.getCategoryTitle());
			category.setCategoryDescription(categoryDto.getCategoryDescription());
			
			categoryReturn = this.categoryRepo.save(category);
		} else {
			throw new ResourceNotFoundException("Category", "Category Id", categoryId);
		}
		
		return this.modelMapper.map(categoryReturn, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Optional<Category> categoryOptional = this.categoryRepo.findById(categoryId);
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			this.categoryRepo.delete(category);
		} else {
			throw new ResourceNotFoundException("Category", "Category Id", categoryId);
		}

	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Optional<Category> categoryOptional = this.categoryRepo.findById(categoryId);
		Category category = null;
		if (categoryOptional.isPresent()) {
			category = categoryOptional.get();
		} else {
			throw new ResourceNotFoundException("Category", "Category Id", categoryId);
		}

		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getCategories() {
		
		List<Category> categories = this.categoryRepo.findAll();
		
		List<CategoryDto> categoryDtos = new ArrayList<>();
		for (Category category : categories) {
			categoryDtos.add(this.modelMapper.map(category, CategoryDto.class));
		}
		
		return categoryDtos;
	}
	
}
