package com.rs.ldap.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rs.ldap.util.JwtTokenUtil;

public class JWTAuthFilter extends OncePerRequestFilter {

	private JwtTokenUtil jwtTokenUtil;

	public JWTAuthFilter(JwtTokenUtil jwtTokenUtil) {
		System.out.println("JWTAuthFilter: 0-param constr");
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpResp, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("doFilter..");

		String token = jwtTokenUtil.resolveToken(httpReq);
		System.out.println("token: " + token);
		try {
			if (token != null && jwtTokenUtil.validateToken(token)) {
				Authentication auth = (token != null) ? jwtTokenUtil.getAuthentication(token) : null;
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			httpResp.sendError(403, e.getMessage());
		}
		filterChain.doFilter(httpReq, httpResp);
		System.out.println("After doFilter");

	}
}
