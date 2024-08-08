package es.taixmiguel.penkatur.core.api.profiles.user.dto;

import java.time.LocalDate;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import es.taixmiguel.penkatur.core.profiles.user.attributes.UserStatus;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotNull long id, @NotBlank String email, @NotBlank String firstName, String secondName,
		@NotBlank String lastName, String secondLastName, @NotNull UserGender gender, @NotNull UserStatus status,
		@NotNull LocalDate dateOfBirth, @NotBlank String avatar) {

	public UserDTO(User user) {
		this(user.getId(), user.getEmail(), user.getFirstName(), user.getSecondName(), user.getLastName(),
				user.getSecondLastName(), user.getGender(), user.getStatus(), user.getDateOfBirth(), user.getAvatar());
	}
}
