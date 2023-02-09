package com.bergin.moonhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.models.dto.LoginDto;
import com.bergin.moonhive.service.GeneralService;
import com.bergin.moonhive.service.UserService;

@RestController
@RequestMapping("/api/no-auth/")
@CrossOrigin(allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS,
		RequestMethod.HEAD, RequestMethod.PUT }, origins = "*", maxAge = 600000)
public class AuthController {
	
	@Autowired
	GeneralService generalService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("create-default-admin")
	public ApiResponse generateDefaultAdmin() {
		return generalService.createAdmin();
	}
	
	@GetMapping("welcome")
	public String welcome() {
		return "Welcome";
	}
	
	@PostMapping("login")
	public ApiResponse login(@RequestBody LoginDto loginDto) {
		return userService.login(loginDto);
	}

}
