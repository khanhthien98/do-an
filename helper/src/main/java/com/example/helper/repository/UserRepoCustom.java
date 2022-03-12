package com.example.helper.repository;

import com.example.helper.controller.request.LoginRequest;

public interface UserRepoCustom {
	public Long loginUser(LoginRequest loginRequest);

	public void updatePassword(String password, Long id);
	
	public Long checkUser(String username);

}
