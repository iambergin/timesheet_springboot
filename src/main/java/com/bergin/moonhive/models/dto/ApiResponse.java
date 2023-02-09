package com.bergin.moonhive.models.dto;

public class ApiResponse {
	private String message;
	private int responseCode;
	private Object response;
	private boolean hasError;
	
	public ApiResponse() {
		super();
	}
	
	public ApiResponse(String message, int responseCode, Object response, boolean hasError) {
		super();
		this.message = message;
		this.responseCode = responseCode;
		this.response = response;
		this.hasError = hasError;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public boolean isHasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	
	
}
