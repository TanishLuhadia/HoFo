package com.hofo.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hofo.entity.User;
import com.hofo.repository.UserRepository;

@RestController
public class UserController {

	private final UserRepository userRepository;
	
	public UserController(UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	
	@PostMapping("/register")
	public User register(@RequestBody User user)
	{
		return userRepository.save(user);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody User user)
	{
	var u=	userRepository.findByUsername(user.getUsername());
		if(!Objects.isNull(u)) return "success";
	return "failure";
	}
}
