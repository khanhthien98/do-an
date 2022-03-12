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
@Table(name = "brand")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
	@Id
	@Column(name = "brand_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long brandID;
	@Column(name = "brand_name")
    private String brandName;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "brand", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Product> listProduct = new ArrayList<>();
}
