package es.taixmiguel.penkatur.core.profiles.user.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${penkatur.security.cors.allowedOrigins}")
	private String[] allowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// @formatter:off
		registry.addMapping("/**")
			.allowedOrigins(allowedOrigins)
			.allowedHeaders("*").allowCredentials(true)
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
		// @formatter:on
	}
}
