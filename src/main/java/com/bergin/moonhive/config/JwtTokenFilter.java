package com.bergin.moonhive.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bergin.moonhive.models.Users;
import com.bergin.moonhive.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
	        if (token != null && jwtTokenProvider.validateToken(token)) {
	            Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }
	        chain.doFilter(request, response);
		
	}


}
