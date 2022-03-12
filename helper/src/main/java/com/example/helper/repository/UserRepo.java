package com.example.helper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Bill;
import com.example.helper.entity.User;

@Repository
public interface UserRepo  extends JpaRepository<User, Long>,UserRepoCustom{

	Page<User> findAll(Specification<User> specification, Pageable pageable);

}
