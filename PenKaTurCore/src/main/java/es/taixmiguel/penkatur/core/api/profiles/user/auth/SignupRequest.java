package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDate;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupRequest(@Email @NotBlank String email, @NotBlank String firstName, String secondName,
		@NotBlank String lastName, String secondLastName, @NotBlank String password, @NotNull UserGender gender,
		@NotNull LocalDate dateOfBirth) {

	public User toUser() {
		return new User(email(), firstName(), secondName(), lastName(), secondLastName(), gender(), dateOfBirth());
	}
}
