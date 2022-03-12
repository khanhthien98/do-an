package com.example.helper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.UserAdmin;

@Repository
public interface UserAdminRepo extends JpaRepository<UserAdmin, Long>, UserAdminRepoCustom {

}
