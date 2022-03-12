package com.example.helper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
	Page<Category> findAll(Specification<Category> specification, Pageable pageable);
}
