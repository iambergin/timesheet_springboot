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
import com.bergin.moonhive.models.dto.CreateUserDto;
import com.bergin.moonhive.models.dto.TimesheetDto;
import com.bergin.moonhive.service.TimesheetService;
import com.bergin.moonhive.service.UserService;

@RestController
@RequestMapping("/api/")
@CrossOrigin(allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS,
		RequestMethod.HEAD, RequestMethod.PUT }, origins = "*", maxAge = 600000)
public class GeneralController {

	@Autowired
	UserService userService;

	@Autowired
	TimesheetService timesheetService;

//
	@GetMapping("admin/user-list")
	public ApiResponse getUserList() {
		return userService.getUsers();
	}

	@PostMapping("admin/create-user")
	public ApiResponse createUser(@RequestBody CreateUserDto createUserDto) {
		return userService.createUser(createUserDto);
	}

	@PostMapping("auth/add-timesheet")
	public ApiResponse addTimesheet(@RequestBody TimesheetDto timesheetDto) {
		return timesheetService.addTimesheet(timesheetDto);
	}

	@GetMapping("auth/get-timesheet")
	public ApiResponse getTimesheet(@RequestParam() String userId) {
		return timesheetService.getTimesheet(userId);
	}
	

	@GetMapping("admin/get-all-timesheet")
	public ApiResponse getTimesheetFoAll() {
		return timesheetService.getAllTimesheet();
	}
}
