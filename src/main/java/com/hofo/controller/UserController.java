package com.hofo.controller;

import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hofo.entity.User;
import com.hofo.repository.UserRepository;
import com.hofo.service.UserService;

@RestController
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;

	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@PostMapping("/register")
	public User register(@RequestBody User user) {

		return userService.register(user);
	}

	@PostMapping("/login")
	public String login(@RequestBody User user) {
		var u = userRepository.findByUsername(user.getUsername());
		if (!Objects.isNull(u))
			return "success";
		return "failure";
	}
}
