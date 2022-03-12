package com.example.helper.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.helper.common.Constants;
import com.example.helper.controller.request.LoginRequest;
import com.example.helper.entity.User;
import com.example.helper.entity.UserAdmin;
import com.example.helper.repository.UserRepoCustom;

public class UserRepoCustomImpl implements UserRepoCustom {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Long loginUser(LoginRequest loginRequest) {
		String sql = "";
		Long result = Constants.LOGIN_FAIL;
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
			sql = "select user_id from users where user_name= :username and user_pass = :password;";
			paramMaps.put("username", loginRequest.getUsername());
			paramMaps.put("password", loginRequest.getPassword());
		}
		if (!sql.isEmpty()) {
			result = namedParameterJdbcTemplate.queryForObject(sql, paramMaps, Long.class);
			return result;
		}
		return result;

	}

	@Override
	public void updatePassword(String password, Long id) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (password != null && id != null) {
			sql = "update users set user_pass = :password where user_id = :id;";
			paramMaps.put("id", id);
			paramMaps.put("password", password);
		}
		if (!sql.isEmpty()) {
			int status = namedParameterJdbcTemplate.update(sql, paramMaps);
			if (status != 0) {
				System.out.println("Change pass successful");
			} else {
				System.out.println("Change pass failure");
			}
		}
	}

	@Override
	public Long checkUser(String username) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (username != null) {
			sql = "select * from users where user_name= :username ;";
			paramMaps.put("username", username);

		}
		if (!sql.isEmpty()) {
			List<User> result = namedParameterJdbcTemplate.query(sql, paramMaps,
					BeanPropertyRowMapper.newInstance(User.class));
			if (result.size() > 0)
				return Constants.LOGIN_SUCCESS;
			return Constants.LOGIN_FAIL;
		}
		return Constants.LOGIN_FAIL;
	}
}
