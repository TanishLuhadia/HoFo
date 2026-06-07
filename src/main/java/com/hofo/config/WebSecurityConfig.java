package com.hofo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
		super();
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	// @Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//	    httpSecurity
//	            .csrf(csrf -> csrf.disable())
//	            .authorizeHttpRequests(
//	                    request -> request
//	                    .requestMatchers("/register","/login").permitAll() 
//	                    .anyRequest().authenticated()
//	            )
//	            .httpBasic(Customizer.withDefaults());
//
//	    return httpSecurity.build();
//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

				.requestMatchers("/register", "/login").permitAll()

				.requestMatchers("/admin/**").hasRole("ADMIN")

				.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

				.anyRequest().authenticated()

		).httpBasic(Customizer.withDefaults()).addFilterBefore(jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// @Bean
//	public UserDetailsService userDetailsService() {
//	    UserDetails tanish = User.withUsername("tanish")
//	            .password("{noop}tanish")
//	            .roles("ADMIN")
//	            .build();
//
//	    UserDetails tanisha = User.withUsername("tanisha")
//	            .password("{noop}tanisha")
//	            .roles("ADMIN")
//	            .build();
//
//	    return new InMemoryUserDetailsManager(tanish, tanisha);
//	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(14);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		// provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setPasswordEncoder(bCryptPasswordEncoder());
		return provider;
	}

	// following code implements only for jwt
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		return configuration.getAuthenticationManager();
	}
}
