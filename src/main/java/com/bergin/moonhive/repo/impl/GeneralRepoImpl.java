package com.bergin.moonhive.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bergin.moonhive.models.Users;


@Repository
public class GeneralRepoImpl {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public Users findUserByEmail(String email) {
		System.out.println(mongoTemplate.collectionExists(Users.class));
		System.out.println(mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), Users.class));
		
		return mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), Users.class);
	}
	
	public List<Users> getListOfUsers() {
		return mongoTemplate.find(new Query(Criteria.where("enabled").is(true)), Users.class);
	}

}
