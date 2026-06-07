package com.hofo.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hofo.entity.Role;
import com.hofo.entity.User;
import com.hofo.entity.VerificationToken;
import com.hofo.repository.RoleRepository;
import com.hofo.repository.UserRepository;
import com.hofo.repository.VerificationTokenRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final EmailService emailService;
	private final VerificationTokenRepository verificationTokenRepository;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService,
			EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.roleRepository = roleRepository;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.verificationTokenRepository = verificationTokenRepository;
	}


	public User register(User user) {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Role role = roleRepository.findByRoleName("USER");

		if (role == null) {
			throw new RuntimeException("USER role not found in database");
		}

		user.setRoles(Set.of(role));

		User savedUser = userRepository.save(user);
		  String token = generateToken(savedUser);
System.out.println("Generated toekn: "+token);
		    emailService.sendActivationEmail(savedUser, token);
		return savedUser;
	}

	public User register(User user, String roleName) {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Role role = roleRepository.findByRoleName(roleName.toUpperCase());

		if (role == null) {
			throw new RuntimeException("Role not found");
		}

		user.setRoles(Set.of(role));

		return userRepository.save(user);
	}

	public String verify(User user) {
		try {
			System.out.println("VERIFY HIT");
			User dbUser = userRepository.findByUsername(user.getUsername());

			if (dbUser == null) {
				return "Invalid username";
			}

			if (!dbUser.isActive()) {
				System.out.println("Verify account");
				return "Please verify the account";

			}

			Authentication authenticate = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

//			var u = userRepository.findByUsername(user.getUsername());
//			if (!Objects.isNull(user))
//				return "14564562321564856213";
//			return "failure";

			System.out.println("AUTHENTICATED = " + authenticate.isAuthenticated());
			System.out.println("AUTHORITIES = " + authenticate.getAuthorities());

			if (authenticate.isAuthenticated()) {
				System.out.println("AUTHENTICATED = " + authenticate.isAuthenticated());
				System.out.println("AUTHORITIES = " + authenticate.getAuthorities());

				return jwtService.generateToken(dbUser);
			}

		} catch (Exception e) {
			System.out.println(e);
			return "Wrong password";

		}
		return "failure";

	}

	public String generateToken(User user) {

	    String token = UUID.randomUUID().toString();

	    VerificationToken vt = verificationTokenRepository.findByUser(user)
	            .orElse(new VerificationToken());

	    vt.setToken(token);
	    vt.setUser(user);
	    vt.setExpiryTime(LocalDateTime.now().plusMinutes(2));

	    verificationTokenRepository.save(vt);

	    return token;
	}
	
}