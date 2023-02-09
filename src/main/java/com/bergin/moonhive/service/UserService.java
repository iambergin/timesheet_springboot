package com.bergin.moonhive.service;

import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.models.dto.CreateUserDto;
import com.bergin.moonhive.models.dto.LoginDto;

public interface UserService {

	public ApiResponse login(LoginDto loginDto);

	String encryptPassword(String rawPass);
	
	public ApiResponse createUser(CreateUserDto createUserDto);

	public ApiResponse getUsers();
}
