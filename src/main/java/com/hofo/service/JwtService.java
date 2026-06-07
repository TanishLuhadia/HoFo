package com.hofo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hofo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey = null;

	public String generateToken(User user) {

		Map<String, Object> claims = new HashMap<>();

		claims.put("roles", user.getRoles().stream().map(role -> role.getRoleName()).toList());

		return Jwts.builder().claims().add(claims).subject(user.getUsername()).issuer("tanish")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000)).and().signWith((Key) generateKey())
				.compact();
	}

	private SecretKey generateKey() {
		byte[] decode = Decoders.BASE64.decode(getSecretKey());
		return Keys.hmacShaKeyFor(decode);
	}

	// get key from environment variable
	public String getSecretKey() {
		return secretKey = "4dfca9230631da7a99a20568a1d8665c64d8cdb65122c6d915d1605aad43697a37b20543fe1952cb91a458b7f9f0ff09";
	}

	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractClaims(String token) {
		return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token).getPayload();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaims(token,Claims::getExpiration);
	}

}
