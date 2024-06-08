package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import es.taixmiguel.penkatur.core.profiles.user.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {

	private UserDetailsServiceImpl userDetailsService;
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring("Bearer ".length());
			if (token != null && jwtTokenUtil.validateToken(token)) {
				String email = jwtTokenUtil.getEmail(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				jwtTokenUtil.setAuthentication(userDetails, request);
			}
		}

		filterChain.doFilter(request, response);
	}

	@Autowired
	public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}
}
