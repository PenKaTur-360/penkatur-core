package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import es.taixmiguel.penkatur.core.profiles.user.security.UserDetailsServiceImpl;
import es.taixmiguel.penkatur.core.tools.log.Log;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {

	private UserDetailsServiceImpl userDetailsService;
	private ToolJWT toolJWT;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = toolJWT.getJwtFromCookies(request);
			if (jwt != null && toolJWT.validateJwtToken(jwt)) {
				String email = toolJWT.getEmailFromJwtToken(jwt);
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			Log.error(getClass(), "Cannot set user authentication: {}", e);
		}

		filterChain.doFilter(request, response);
	}

	@Autowired
	public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void setToolJWT(ToolJWT toolJWT) {
		this.toolJWT = toolJWT;
	}
}
