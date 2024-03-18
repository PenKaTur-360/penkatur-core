package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SigninRequest {

	private @Email @NotBlank String email;
	private @NotBlank String password;
}
