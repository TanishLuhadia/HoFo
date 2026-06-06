package com.hofo.service;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hofo.entity.Role;
import com.hofo.entity.User;
import com.hofo.repository.RoleRepository;
import com.hofo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public User register(User user) {

        user.setPassword(
                bCryptPasswordEncoder.encode(user.getPassword()));

        Role role =
                roleRepository.findByRoleName("USER");

        if(role == null) {
            throw new RuntimeException(
                    "USER role not found in database");
        }

        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }
    
    public User register(User user, String roleName) {

        user.setPassword(
                bCryptPasswordEncoder.encode(user.getPassword()));

        Role role =
                roleRepository.findByRoleName(
                        roleName.toUpperCase());

        if(role == null) {
            throw new RuntimeException("Role not found");
        }

        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }
    
}