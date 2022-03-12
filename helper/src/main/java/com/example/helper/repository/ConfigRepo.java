package com.example.helper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helper.entity.Config;

@Repository
public interface ConfigRepo extends JpaRepository<Config, Long>{
	public Config getByName(String name);
}
