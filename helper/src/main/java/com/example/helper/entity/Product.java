package com.example.helper.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@Column(name = "product_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productID;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@Column(name = "product_name")
	private String productName;
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;
	@Column(name = "product_image")
	private String productImage;
	@Column(name = "product_image_forward")
	private String productImageForward;
	@Column(name = "product_image_back")
	private String productImageBack;
	@Column(name = "product_price")
	private long productPrice;
	@Column(name = "product_description")
	private String productDescription;
	@Column(name = "product_importprice")
	private long productImportPrice;
	@Column(name = "product_quantily")
	private int productQuantily;
	@Column(name = "product_cpu")
	private String productCPU;
	@Column(name = "product_ram")
	private String productRAM;
	@Column(name = "product_harddrive")
	private String productHardDrive;
	@Column(name = "product_screen")
	private String productScreen;
	@Column(name = "product_cardscreen")
	private String productCardScreen;
	@Column(name = "product_os")
	private String productOS;
	@Column(name = "product_dimensions")
	private String productDimensions;
	@Column(name = "product_weight")
	private byte productWeight;
	@Column(name = "product_material")
	private String productMaterial;
	@Column(name = "product_resolution")
	private String productResolution;
	@Column(name = "product_quantity_sold")
	private int productQuantitySold;
	@Transient
	private int quanlityBuy;

}
