package com.furniture.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value(value = "${app.jwtSecret}")
	private String jwtSecret;

	@Value(value = "${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	public String generateToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
				.setSubject(Long.toString(userPrincipal.getId()))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();

		return Long.valueOf(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(authToken);
			return true;
		} catch (SecurityException ex) {
			LOGGER.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty");
		}
		return false;
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
