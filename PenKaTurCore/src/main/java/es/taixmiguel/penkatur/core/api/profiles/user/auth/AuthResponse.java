package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthResponse(@NotNull long idUser, @NotBlank String message, TokenDTO accessToken,
		TokenDTO refreshToken) {

	public AuthResponse(@NotNull long idUser, @NotBlank String message, @NotNull UserToken accessToken,
			@NotNull UserToken refreshToken) {
		this(idUser, message, new TokenDTO(accessToken), new TokenDTO(refreshToken));
	}

	public AuthResponse(String message) {
		this(0, message, (TokenDTO) null, (TokenDTO) null);
	}
}
