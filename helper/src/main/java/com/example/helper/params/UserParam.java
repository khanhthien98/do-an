package com.example.helper.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserParam extends BaseParam {
	private String userName;
	private long phone;
	private String email;
}
