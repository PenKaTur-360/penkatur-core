package es.taixmiguel.penkatur.core.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI openAPI() {
		Contact contact = new Contact();
		contact.setName("TaixMiguel");
		contact.setUrl("https://github.com/TaixMiguel");
		License apacheLicense = new License().name("Apache License, Version 2.0")
				.url("https://www.apache.org/licenses/LICENSE-2.0");
		Info info = new Info().title("PenKaTur API").version("1.0").contact(contact).license(apacheLicense);
		return new OpenAPI().info(info);
	}
}
