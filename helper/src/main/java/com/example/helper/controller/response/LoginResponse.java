package com.example.helper.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String auth;
	private String username;
	private Long userId;
	public LoginResponse(String auth, String username) {
		super();
		this.auth = auth;
		this.username = username;
	}
	
	
}
