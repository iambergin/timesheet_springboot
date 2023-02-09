package com.bergin.moonhive.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.bergin.moonhive.models.Timesheet;
import com.bergin.moonhive.models.Users;
import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.models.dto.TimesheetDto;
import com.bergin.moonhive.repo.TimesheetRepo;
import com.bergin.moonhive.repo.UserRepo;
import com.bergin.moonhive.service.TimesheetService;

@Service
public class TimesheetServiceImpl implements TimesheetService {

	@Autowired
	TimesheetRepo timesheetRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public ApiResponse addTimesheet(TimesheetDto timesheetDto) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd");
			Timesheet timesheet = new Timesheet();
			timesheet.setTitle(timesheetDto.getTitle());
			timesheet.setProject(timesheetDto.getProject());
			timesheet.setDate(dateFormat.parse(timesheetDto.getDate()));
			timesheet.setHrs(timesheetDto.getHrs());
			Optional<Users> user = userRepo.findById(timesheetDto.getUserId());
			if (user.isPresent()) {
				timesheet.setUser(user.get());
				timesheet = timesheetRepo.save(timesheet);
				timesheetDto.setId(timesheet.getId());
				return new ApiResponse("Success", 200, timesheetDto, false);
			} else {
				return new ApiResponse("User Details missing", 200, null, true);
			}
		} catch (ParseException e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}

	@Override
	public ApiResponse getTimesheet(String userId) {
		try {
			List<Timesheet> timesheetList = mongoTemplate
					.find(new Query(Criteria.where("user.$id").is(new ObjectId(userId))), Timesheet.class);
			List<Timesheet> res = timesheetList.stream().map(e -> {
				e.setUser(null);
				return e;
			}).collect(Collectors.toList());
			return new ApiResponse("Success", 200, res, false);
		} catch (Exception e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}

	@Override
	public ApiResponse getAllTimesheet() {
		try {
			List<Timesheet> timesheetList = mongoTemplate
					.find(new Query(), Timesheet.class);
			List<Timesheet> res = timesheetList.stream().map(e -> {
				e.getUser().setPassword(null);
				return e;
			}).collect(Collectors.toList());
			return new ApiResponse("Success", 200, res, false);
		} catch (Exception e) {
			return new ApiResponse("Something went wrong", 500, null, true);
		}
	}
	

}
