package com.example.helper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Review;

@Repository
public interface ReviewRepo  extends JpaRepository<Review, Long>{

}
