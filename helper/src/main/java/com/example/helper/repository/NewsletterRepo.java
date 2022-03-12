package com.example.helper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Newsletter;

@Repository
public interface NewsletterRepo  extends JpaRepository<Newsletter, Long>{

}
