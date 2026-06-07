package com.hofo.controller;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
@Controller
public class LoginController {

	@GetMapping("/csrf")
	public CsrfToken getToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	@GetMapping("/login")
	public String getProduct() {
		return "login";
	}

}
