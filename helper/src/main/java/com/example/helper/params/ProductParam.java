package com.example.helper.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductParam extends BaseParam {
	private long productID;
	private long categoryID;
	private long brandID;
	private String productName;
	private long priceFrom;
	private long priceTo;
	private boolean inventory;

}
