package com.hofo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hofo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String userName);
}
