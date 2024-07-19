package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.ZonedDateTime;

import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenDTO(@NotBlank ZonedDateTime tokenExpirationDate, @NotBlank String token) {

	public TokenDTO(@NotNull UserToken uToken) {
		this(uToken.getExpiryDate(), uToken.getToken());
	}
}
