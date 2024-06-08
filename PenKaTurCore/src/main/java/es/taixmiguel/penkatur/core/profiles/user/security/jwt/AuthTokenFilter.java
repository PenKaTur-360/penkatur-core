package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import es.taixmiguel.penkatur.core.profiles.user.security.UserDetailsServiceImpl;
import es.taixmiguel.penkatur.core.profiles.user.service.UserTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {

	private UserDetailsServiceImpl userDetailsService;
	private UserTokenService tokenService;
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring("Bearer ".length());
			if (token != null && jwtTokenUtil.validateToken(token)) {
				tokenService.findByAccessToken(token).map(tokenService::verifyExpiration);
				String username = jwtTokenUtil.getUsername(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
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
	protected void setTokenService(UserTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Autowired
	public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}
}
