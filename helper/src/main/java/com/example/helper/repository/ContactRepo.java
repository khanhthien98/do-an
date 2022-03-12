package com.example.helper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Contact;

@Repository
public interface ContactRepo  extends JpaRepository<Contact, Long>{
	Page<Contact> findAll(Specification<Contact> specification, Pageable pageable);
}
