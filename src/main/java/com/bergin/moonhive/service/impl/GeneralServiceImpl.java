package com.bergin.moonhive.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bergin.moonhive.models.Roles;
import com.bergin.moonhive.models.Users;
import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.repo.RoleRepo;
import com.bergin.moonhive.repo.UserRepo;
import com.bergin.moonhive.service.GeneralService;
import com.bergin.moonhive.service.UserService;

@Service
public class GeneralServiceImpl implements GeneralService {

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	UserService userService;
	
	@Override
	public ApiResponse createAdmin() {
		ApiResponse response = new ApiResponse();
		String[] defaultRoles = {"ADMIN", "USER"};
		try {
			for (String role : defaultRoles) {
				Roles roles = new Roles();
				roles.setRole(role);
				roleRepo.save(roles);
			}
			Users users = new Users();
			users.setEmail("19.bergin@gmail.com");
			users.setFullname("Bergin");
			users.setEnabled(true);
			users.setPassword(userService.encryptPassword("bergin@123"));
			Set<Roles> role_list = new HashSet<>();
			Roles role = roleRepo.findByRole("ADMIN");
			role_list.add(role);
			users.setRoles(role_list);
			userRepo.save(users);
			response.setHasError(false);
			response.setResponse(null);
			response.setResponseCode(200);
			response.setMessage("Default Data created successfully!!");
		} catch (Exception e) {
			response.setHasError(true);
			response.setResponse(null);
			response.setResponseCode(500);
			response.setMessage(e.getMessage());
		}
		return response;
	}

}
