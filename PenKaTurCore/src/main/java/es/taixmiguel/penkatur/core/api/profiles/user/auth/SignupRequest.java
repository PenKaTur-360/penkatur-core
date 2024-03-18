package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDate;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

	private @Email @NotBlank String email;
	private @NotBlank String firstName;
	private String secondName;
	private @NotBlank String lastName;
	private String secondLastName;
	private @NotBlank String password;
	private @NotNull UserGender gender;
	private @NotNull LocalDate dateOfBirth;

	public User toUser() {
		return new User(getEmail(), getFirstName(), getSecondName(), getLastName(), getSecondLastName(), getGender(),
				getDateOfBirth());
	}
}
