package com.example.helper.entity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@Column(name = "user_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
	@Column(name = "user_name")
    private String userName;
	@Column(name = "user_email")
    private String userEmail;
	@Column(name = "user_pass")
    private String userPass;
	@Column(name = "user_role")
    private boolean userRole;
	@Column(name = "user_phone")
    private String userPhone;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Bill> listBill = new ArrayList<>();
}
