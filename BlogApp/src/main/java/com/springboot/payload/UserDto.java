package com.springboot.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDto {

	private int id;
	@NotEmpty
	@Size(min = 4, message = "Username must be minimum of 4 characters")
	private String name;
	
	@Email(message = "Email address is not valid")
	@NotEmpty
	private String email;
	@NotEmpty
	@Size(min = 3, max = 10, message = "Password must be minimum of 3 characters and maximum of 10 characters")
	private String password;
	@NotEmpty
	private String about;
	
}
