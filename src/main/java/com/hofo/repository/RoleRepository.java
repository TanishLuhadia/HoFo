package com.hofo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hofo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName);
}