package com.hofo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hofo.entity.User;
import com.hofo.service.UserService;

public class AdminController {
	private final UserService userService;

	public AdminController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/admin/create-user")
	@PreAuthorize("hasRole('ADMIN')")
	public User createUser(@RequestBody User user, @RequestParam String role) {

		return userService.register(user, role);
	}

}
