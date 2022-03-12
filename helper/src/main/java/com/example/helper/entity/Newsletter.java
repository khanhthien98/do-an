package com.example.helper.entity;
import java.sql.Timestamp;

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
@Table(name = "newsletter")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Newsletter {
	@Id
	@Column(name = "newsletter_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long newsletterID;
	@Column(name = "newsletter_email")
    private String newsletterName;
	@Column(name = "date")
    private Timestamp date;
}
