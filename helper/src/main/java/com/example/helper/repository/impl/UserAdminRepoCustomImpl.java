package com.example.helper.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.helper.common.Constants;
import com.example.helper.controller.request.LoginRequest;
import com.example.helper.entity.UserAdmin;
import com.example.helper.repository.UserAdminRepoCustom;

@Repository
public class UserAdminRepoCustomImpl implements UserAdminRepoCustom {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Long loginAdmin(LoginRequest loginRequest) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
			sql = "select * from tbluser where user_name= :username and user_pass = md5(:password);";
			paramMaps.put("username", loginRequest.getUsername());
			paramMaps.put("password", loginRequest.getPassword());
		}
		if (!sql.isEmpty()) {
			List<UserAdmin> result = namedParameterJdbcTemplate.query(sql, paramMaps, BeanPropertyRowMapper.newInstance(UserAdmin.class));
			if (result.size()>0)
				return Constants.LOGIN_SUCCESS;
			return Constants.LOGIN_FAIL;
		}
		return Constants.LOGIN_FAIL;

	}

}
