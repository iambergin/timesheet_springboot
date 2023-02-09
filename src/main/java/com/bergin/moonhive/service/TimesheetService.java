package com.bergin.moonhive.service;

import com.bergin.moonhive.models.dto.ApiResponse;
import com.bergin.moonhive.models.dto.TimesheetDto;

public interface TimesheetService {
	
	public ApiResponse addTimesheet(TimesheetDto timesheetDto);

	public ApiResponse getTimesheet(String userId);
	
	public ApiResponse getAllTimesheet();
	
}
