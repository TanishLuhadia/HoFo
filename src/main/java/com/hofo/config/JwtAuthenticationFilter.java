package com.hofo.config;

import java.io.IOException;
import java.net.Authenticator;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hofo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getRequestURI();

		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer") ||   path.equals("/register") || path.equals("/verify")){
			filterChain.doFilter(request, response);
			return;
		}
		final String jwt = authHeader.substring(7);
		final String userName = jwtService.extractUserName(jwt);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (userName != null && authentication == null) {
			// authenticate
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken autheticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				autheticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(autheticationToken);
			}
		}
		filterChain.doFilter(request, response);

	}

}
