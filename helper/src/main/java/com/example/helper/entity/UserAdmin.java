package com.example.helper.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbluser")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdmin {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	@Column(name = "user_name")
	private String user_name;
	@Column(name = "user_pass")
	private String user_pass;
	@Column(name = "user_fullname")
	private String user_fullname;
	@Column(name = "user_birthday")
	private String user_birthday;
	@Column(name = "user_mobilephone")
	private String user_mobilephone;
	@Column(name = "user_homephone")
	private String user_homephone;
	@Column(name = "user_officephone")
	private String user_officephone;
	@Column(name = "user_email")
	private String user_email;
	@Column(name = "user_address")
	private String user_address;
	@Column(name = "user_jobarea")
	private String user_jobarea;
	@Column(name = "user_job")
	private String user_job;
	@Column(name = "user_position")
	private String user_position;
	@Column(name = "user_applyyear")
	private Long user_applyyear;
	@Column(name = "user_permission")
	private byte user_permission;
	@Column(name = "user_notes")
	private String user_notes;
	@Column(name = "user_roles")
	private String user_roles;
	@Column(name = "user_logined")
	private Long user_logined;
	@Column(name = "user_created_date")// sá»‘ láº§n login
	private String user_created_date;
	@Column(name = "user_last_modified")
	private String user_last_modified;
	@Column(name = "user_last_logined")
	private String user_last_logined;
	@Column(name = "user_parent_id")
	private Long user_parent_id;
	@Column(name = "user_actions")
	private Long user_actions;

}
