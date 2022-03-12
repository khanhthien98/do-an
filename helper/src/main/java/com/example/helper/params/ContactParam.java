package com.example.helper.params;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactParam extends BaseParam {

	private long contactID;
	private String contactName;
	private String contactWeb;
	private String contactEmail;
	private String contactTitle;
	private String contactMessage;
	private Date date;
}
