package com.example.helper.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "review")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	@Id
	@Column(name = "review_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewID;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(name = "review_name")
    private String reviewName;
	@Column(name = "review_email")
    private String reviewEmail;
	@Column(name = "review_star")
    private int reviewStar;
	@Column(name = "review_message")
    private String reviewMessage;
}
