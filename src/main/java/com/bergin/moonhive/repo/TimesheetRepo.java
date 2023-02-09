package com.bergin.moonhive.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bergin.moonhive.models.Timesheet;

public interface TimesheetRepo extends MongoRepository<Timesheet, String>{

}
