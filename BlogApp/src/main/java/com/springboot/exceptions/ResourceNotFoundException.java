package com.springboot.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotFoundException extends RuntimeException {
	
	String resourceName;
	String fieldName;
	long fieldValue;
	String feildValueString;
	
	public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, String fieldValueString) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValueString));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.feildValueString = fieldValueString;
	}
}
