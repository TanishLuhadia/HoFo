package com.hofo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hofo.entity.User;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendWelcomeEmail(String toEmail, String username) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Welcome to HoFo 🍽️");

		message.setText("Hi " + username + ",\n\n" + "Welcome to HoFo!\n" + "Your registration was successful.\n\n"
				+ "Enjoy smart dining experience 🚀");

		mailSender.send(message);
	}

	public void sendActivationEmail(User user, String token) {

		String link = "http://localhost:8080/verify?token=" + token;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Activate your HoFo account");

		message.setText("Hi " + user.getUsername() + ",\n\n" + "Welcome to HoFo!\n"
				+ "Click below link to activate your account:\n" + link + "\n\n"
				+ "This link is valid for 2 minutes only.");

		mailSender.send(message);
	}

	public void sendSuccessfull(User user) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Welcome to HoFo 🍽️");

		message.setText("Hi " + user.getUsername() + ",\n\n" + "Welcome to HoFo! 🎉\n\n"
				+ "Your account has been successfully created and you are now part of our growing food experience platform.\n\n"
				+ "At HoFo, we aim to make your dining experience smarter, faster, and more enjoyable.\n\n"
				+ "You can now explore restaurants, book tables, and enjoy seamless ordering.\n\n"
				+ "If you have any questions or need support, feel free to reach out anytime.\n\n"
				+ "We’re excited to have you with us! 🚀\n\n" + "Happy Dining,\n" + "Team HoFo");

		mailSender.send(message);
	}
}