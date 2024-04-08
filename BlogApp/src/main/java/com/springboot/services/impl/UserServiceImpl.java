package com.springboot.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.User;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.payload.UserDto;
import com.springboot.repositories.UserRepo;
import com.springboot.services.UserService;

import jakarta.validation.constraints.NotNull;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		System.out.println(user);
		System.out.println(userDto);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		UserDto userDtoReturn = null;
		Optional<User> userOptional = this.userRepo.findById(userId);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

				user.setEmail(userDto.getEmail());
				user.setAbout(userDto.getAbout());
				user.setName(userDto.getName());
				user.setPassword(userDto.getPassword());
			User updatedUser = this.userRepo.save(user);
			userDtoReturn = this.userToDto(updatedUser);
		} else {
			throw new ResourceNotFoundException("User", "id", userId);
		}

		return userDtoReturn;
	}

	@Override
	public UserDto getUserById(Integer userId) {

		Optional<User> userOptional = this.userRepo.findById(userId);
		User user = null;
		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			throw new ResourceNotFoundException("User", "id", userId);
		}
		UserDto userDto = this.userToDto(user);

		return userDto;
	}

	@Override
	public List<UserDto> getAllUser() {

		List<User> users = this.userRepo.findAll();

		List<UserDto> userDtos = new ArrayList<>();
		for (User user : users) {
			userDtos.add(this.userToDto(user));
		}
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		Optional<User> userOptional = this.userRepo.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			this.userRepo.delete(user);
		} else {
			throw new ResourceNotFoundException("User", "id", userId);
		}
	}

	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);

//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setAbout(userDto.getAbout());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());

		return user;
	}

	private UserDto userToDto(User user) {

		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}
}
