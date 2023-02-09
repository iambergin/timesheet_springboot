package com.bergin.moonhive.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bergin.moonhive.models.Users;

public interface UserRepo extends  MongoRepository<Users, String> {
	 Users findByEmail(String email);
}
