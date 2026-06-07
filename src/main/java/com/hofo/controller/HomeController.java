package com.hofo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		try {
			System.out.println("Returning landing page");
			return "landingpage";

		} catch (Exception e) {
			System.out.println(e);
		}
		return "Some problem";
	}
}
