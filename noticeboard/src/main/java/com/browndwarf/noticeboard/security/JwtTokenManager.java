package com.browndwarf.noticeboard.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.browndwarf.noticeboard.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenManager {

	static private String secretKey = "browndwarf";
	
	//static private long expiredTime = 1000L * 60 * 60 * 24;
	
	@Autowired
	UserService	userService;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String userId, String role) {
		Claims claims = Jwts.claims().setId(userId);
	        
		claims.put("role", role);
	        
		return Jwts.builder()
				.setClaims(claims) // Data
				.setIssuedAt(new Date()) // Issue data
	                //.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, secretKey) // Encryption Algorithm, Configure secret
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userService.loadUserByUsername(this.getUserName(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getUserName(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getId();
	}
	
	public String getTokenFromHeader(HttpServletRequest req) {
		return req.getHeader("X-AUTH-KEY");
	}
	
	public boolean validateToken(String token) {
		return true;
	}
}
