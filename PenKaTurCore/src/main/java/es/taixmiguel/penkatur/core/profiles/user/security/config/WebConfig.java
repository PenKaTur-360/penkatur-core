package es.taixmiguel.penkatur.core.profiles.user.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import es.taixmiguel.penkatur.core.profiles.user.security.interceptor.UserAccessInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${penkatur.security.cors.allowedOrigins}")
	private String[] allowedOrigins;

	@Autowired
	private UserAccessInterceptor userAccessInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// @formatter:off
		registry.addMapping("/**")
			.allowedOrigins(allowedOrigins)
			.allowedHeaders("*").allowCredentials(true)
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
		// @formatter:on
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// @formatter:off
		registry.addInterceptor(userAccessInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns(
				"/api/auth/**", "/api/info",
				"/swagger-ui/**", "/v3/api-docs/**",
				"/error", "/error/**", "/h2-console/**"
			);
		// @formatter:on
	}
}
