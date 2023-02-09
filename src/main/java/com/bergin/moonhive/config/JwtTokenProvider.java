package com.bergin.moonhive.config;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.bergin.moonhive.models.Roles;
import com.bergin.moonhive.service.UserService;
import com.bergin.moonhive.service.impl.UserServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Autowired UserServiceImpl userService;
	
	protected String encSecret(String secret) {
	    return Base64.getEncoder().encodeToString(secret.getBytes());
	}
		
	public String createToken(String username, Set<Roles> set) {
	    Claims claims = Jwts.claims().setSubject(username);
	    claims.put("roles", set);
	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);
	    return Jwts.builder()//
	        .setClaims(claims)//
	        .setIssuedAt(now)//
	        .setExpiration(validity)//
	        .signWith(SignatureAlgorithm.HS256, encSecret(secretKey))//
	        .compact();
	}
	
	public String getUsername(String token) {
	    return Jwts.parser().setSigningKey(encSecret(secretKey)).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest req) {
		req.getHeaderNames().asIterator().forEachRemaining((e) -> {System.out.println("Inside Header > " + e);});
	    String bearerToken = req.getHeader("Authorization");
	    System.out.println("bearerToken > " + bearerToken);
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7, bearerToken.length());
	    }
	    return null;
	}
	
	public boolean validateToken(String token) {
	    try {
	        Jws<Claims> claims = Jwts.parser().setSigningKey(encSecret(secretKey)).parseClaimsJws(token);
	        if (claims.getBody().getExpiration().before(new Date())) {
	            return false;
	        }
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new JwtException("Expired or invalid JWT token");
	    }
	}
	
	public Authentication getAuthentication(String token) {
		System.out.println(getUsername(token));
		UserDetails userDetails = this.userService.loadUserByUsername(getUsername(token));
		System.out.println(userDetails.getAuthorities());
	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

}
