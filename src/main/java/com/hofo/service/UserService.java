package com.hofo.service;

import java.util.Objects;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.roleRepository = roleRepository;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public User register(User user) {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Role role = roleRepository.findByRoleName("USER");

		if (role == null) {
			throw new RuntimeException("USER role not found in database");
		}

		user.setRoles(Set.of(role));

		return userRepository.save(user);
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

			Authentication authenticate = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

//			var u = userRepository.findByUsername(user.getUsername());
//			if (!Objects.isNull(user))
//				return "14564562321564856213";
//			return "failure";

			System.out.println("AUTHENTICATED = " + authenticate.isAuthenticated());
			System.out.println("AUTHORITIES = " + authenticate.getAuthorities());

			if (authenticate.isAuthenticated()) {
				User dbUser = userRepository.findByUsername(user.getUsername());

				System.out.println("AUTHENTICATED = " + authenticate.isAuthenticated());
				System.out.println("AUTHORITIES = " + authenticate.getAuthorities());

				return jwtService.generateToken(dbUser);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return "failure";

	}
}