package es.taixmiguel.penkatur.core.profiles.user.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.taixmiguel.penkatur.core.profiles.user.security.jwt.AuthEntryPointJWT;
import es.taixmiguel.penkatur.core.profiles.user.security.jwt.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Value("${penkatur.security.cors.allowedOrigins}")
	private List<String> allowedOrigins;

	@Value("${penkatur.security.jwt.cookieExpiration}")
	private int cookieExpiration;

	private AuthEntryPointJWT unauthorizedHandler;
	private UserDetailsService userDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http.cors(cors -> cors.configurationSource(corsConfigSource()))
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/error/**", "/h2-console/**").permitAll()
					.requestMatchers("/api/auth/**", "/api/info").permitAll()
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.anyRequest().authenticated()); 
		// @formatter:on

		http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
		http.authenticationProvider(authenticationProvider());
		return http.build();
	}

	@Bean
	AuthTokenFilter authenticationTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig) throws Exception {
		return authenticationConfig.getAuthenticationManager();
	}

	CorsConfigurationSource corsConfigSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedHeaders(List.of("Authorization"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowedMethods(List.of("GET", "POST"));
		config.setMaxAge((long) cookieExpiration);
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void setUnauthorizedHandler(AuthEntryPointJWT unauthorizedHandler) {
		this.unauthorizedHandler = unauthorizedHandler;
	}
}
