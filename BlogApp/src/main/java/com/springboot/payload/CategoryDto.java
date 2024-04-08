package com.springboot.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CategoryDto {

	private Integer categoryId;
	@NotEmpty
	private String categoryTitle;
	@NotEmpty
	private String categoryDescription;
}
