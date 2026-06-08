package com.hofo.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hofo.dto.ActionResponse;
import com.hofo.entity.User;
import com.hofo.entity.VerificationToken;
import com.hofo.repository.UserRepository;
import com.hofo.repository.VerificationTokenRepository;
import com.hofo.service.EmailService;
import com.hofo.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;
	private final VerificationTokenRepository verificationTokenRepository;
	private final EmailService emailService;

	public UserController(UserRepository userRepository, UserService userService,
			VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.userService = userService;
		this.verificationTokenRepository = verificationTokenRepository;
		this.emailService = emailService;
	}

	@ResponseBody
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		System.out.println("@@@@@@@");
		return userService.register(user);
	}

	@GetMapping("/registerPage")
	public String registerPage() {
		return "registration";
	}

	@ResponseBody
	@PostMapping("/login")
	public ActionResponse login(@RequestBody User user,HttpServletResponse res) {
//		var u = userRepository.findByUsername(user.getUsername());
//		if (!Objects.isNull(u))
//			return "success";
//		return "failure";

		String response = userService.verify(user);
		ActionResponse actionResponse = new ActionResponse();
		actionResponse.setException(null);

		if (response.equalsIgnoreCase("failure")) {
			actionResponse.setResult("failure");
			actionResponse.setSuccessful(false);
		} else if (response.equalsIgnoreCase("Please verify the account")) {
			actionResponse.setResult("Please verify the account");
			actionResponse.setException("Please verify the account");
			actionResponse.setSuccessful(false);
		} else if (response.equalsIgnoreCase("Invalid username")) {
			actionResponse.setResult("Invalid username");
			actionResponse.setException("Invalid username");
			actionResponse.setSuccessful(false);

		} else if (response.equalsIgnoreCase("Wrong password")) {
			actionResponse.setResult("Wrong password");
			actionResponse.setException("Wrong password");
			actionResponse.setSuccessful(false);

		}

		else {
			actionResponse.setResult(response);
			actionResponse.setSuccessful(true);
		    Cookie cookie = new Cookie("jwt",response);
		    cookie.setHttpOnly(true);
		    cookie.setPath("/");
		    cookie.setMaxAge(60 * 60 * 24); // 1 day
		    res.addCookie(cookie);

		}

		return actionResponse;
	}

	@GetMapping("/check-username")
	@ResponseBody
	public boolean checkUsername(@RequestParam String username) {
		return !userRepository.existsByUsername(username);
	}

	@GetMapping("/check-email")
	@ResponseBody
	public boolean checkEmail(@RequestParam String email) {
		return !userRepository.existsByEmail(email);
	}

	@GetMapping("/verify")
	public String verifyUser(@RequestParam("token") String token, Model model) {
		try {
			System.out.println("INSIDE VERIFY ENDPOINT, token: "+token);
			VerificationToken vt = verificationTokenRepository.findByToken(token).orElse(null);

			if (vt == null) {
				model.addAttribute("message", "Invalid activation link");
				return "activation-error";
			}

			// expired token
			if (vt.getExpiryTime().isBefore(LocalDateTime.now())) {

				String newToken = userService.generateToken(vt.getUser());
				emailService.sendActivationEmail(vt.getUser(), newToken);

				model.addAttribute("message", "Link expired. A new activation link has been sent to your email.");

				return "activation-expired";
			}

			// activate user
			User user = vt.getUser();
			user.setActive(true);
			userRepository.save(user);

			model.addAttribute("username", user.getUsername());
			emailService.sendSuccessfull(vt.getUser());

			return "activation-success";

		} catch (Exception e) {
			System.out.println(e);
			return "login";
		}

	}

	
	@GetMapping("/restaurant-data")
	public String restaurant()
	{
		return "restaurant";
	}
}
