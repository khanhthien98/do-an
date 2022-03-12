package com.example.helper.params;

import lombok.Data;

@Data
public class BaseParam {
	private int pageIndex;
	private int pageSize;
	private String sortField;
	private String sortOrder;
}
