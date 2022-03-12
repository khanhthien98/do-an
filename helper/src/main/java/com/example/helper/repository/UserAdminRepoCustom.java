package com.example.helper.repository;

import com.example.helper.controller.request.LoginRequest;

public interface UserAdminRepoCustom {
	public Long loginAdmin(LoginRequest loginRequest);
}
