package com.example.helper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Bill;

@Repository
public interface BillRepo  extends JpaRepository<Bill, Long>{
	Page<Bill> findAll(Specification<Bill> specification, Pageable pageable);
}
