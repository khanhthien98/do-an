package com.example.helper.params;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillParam extends BaseParam{
	private long billID;
	private Date fromDate;
	private Date toDate;
	private long priceFrom;
	private long priceTo;
	private String userName;
	private long userId;
}
