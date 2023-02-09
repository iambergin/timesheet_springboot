package com.bergin.moonhive.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bergin.moonhive.models.Roles;

public interface RoleRepo extends MongoRepository<Roles, String> {
	Roles findByRole(String role);
}
