package com.bergin.moonhive.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bergin.moonhive.config.JwtTokenProvider;
import com.bergin.moonhive.models.Roles;
import com.bergin.moonhive.models.Timesheet;
import com.bergin.moonhive.models.Users;
import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.models.dto.BasicUserDetails;
import com.bergin.moonhive.models.dto.CreateUserDto;
import com.bergin.moonhive.models.dto.LoginDto;
import com.bergin.moonhive.repo.RoleRepo;
import com.bergin.moonhive.repo.UserRepo;
import com.bergin.moonhive.repo.impl.GeneralRepoImpl;
import com.bergin.moonhive.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepo userRepository;
	
	@Autowired
	private GeneralRepoImpl generalRepoImpl;

	@Autowired
	private RoleRepo roleRepository;
	
		
	private final
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

	
	public Users findUserByEmail(String email) {
	    return generalRepoImpl.findUserByEmail(email);
	}
	
	public void saveUser(Users user) {
	    user.setPassword(encryptPassword(user.getPassword()));
	    user.setEnabled(true);
	    Roles userRole = roleRepository.findByRole("ADMIN");
	    user.setRoles(new HashSet<>(Arrays.asList(userRole)));
	    userRepository.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("Username > " + email);
	    Users user = generalRepoImpl.findUserByEmail(email);
	    System.out.println("user > " + user);
	    if(user != null) {
	        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
	        return buildUserForAuthentication(user, authorities);
	    } else {
	        throw new UsernameNotFoundException("username not found");
	    }
	}
	


	private List<GrantedAuthority> getUserAuthority(Set<Roles> userRoles) {
	    Set<GrantedAuthority> roles = new HashSet();
	    userRoles.forEach((role) -> {
	        roles.add(new SimpleGrantedAuthority(role.getRole()));
	    });

	    List<GrantedAuthority> grantedAuthorities = new ArrayList(roles);
	    return grantedAuthorities;
	}
	
	private UserDetails buildUserForAuthentication(Users user, List<GrantedAuthority> authorities) {
	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
	
	
	@Override
	public String encryptPassword(String rawPass) {
		return encoder.encode(rawPass);
	}

	@Override
	public ApiResponse login(LoginDto loginDto) {
		Users user = findUserByEmail(loginDto.getUsername());
		System.out.println("User >> " + user);
		try {
			if (user != null) {
				BasicUserDetails userRes = new BasicUserDetails();
				if(encoder.matches(loginDto.getPassword(), user.getPassword())) {
					String token = new JwtTokenProvider().createToken(user.getEmail(), user.getRoles());
					userRes.setEmail(user.getEmail());
					userRes.setFullName(user.getFullname());
					userRes.setRoles(user.getRoles().stream().map(e -> e.getRole()).collect(Collectors.toSet()));
					userRes.setToken(token);
					userRes.setUserId(user.getId());
					return new ApiResponse("Success", 200, userRes, false);
				} else {
					return new ApiResponse("Incorrect Password", 200, userRes, true);
				}
			} else {
				return new ApiResponse("User Not found", 200, null, true);
			}
		} catch (Exception e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}

	@Override
	public ApiResponse createUser(CreateUserDto createUserDto) {
		try {
			Users user = new Users();
			user.setPassword(encryptPassword(createUserDto.getPassword()));
			user.setEmail(createUserDto.getEmail());
			user.setEnabled(true);
			user.setFullname(createUserDto.getFullName());
		    user.setEmail(createUserDto.getEmail());
		    Roles userRole = roleRepository.findByRole("USER");
		    user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		    userRepository.save(user);
		    return new ApiResponse("Success", 200, createUserDto, false);
		}catch (Exception e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}

	@Override
	public ApiResponse getUsers() {
		try {
			List<Users> usersList = generalRepoImpl.getListOfUsers();
			List<Users> res = usersList.stream().map(e -> {
				e.setPassword(null);
				return e;
			}).collect(Collectors.toList());
		    return new ApiResponse("Success", 200, res, false);
		}catch (Exception e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}

}
