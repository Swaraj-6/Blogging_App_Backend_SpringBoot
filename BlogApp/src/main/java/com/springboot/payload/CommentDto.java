package com.springboot.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

	private Integer Id;
	
	private String content;
	
	private Integer userId;
}
