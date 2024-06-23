package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.Instant;

public record AuthResponse(long idUser, String message, String token, String refreshToken, Instant timestamp) {

	public AuthResponse(long idUser, String message, String token, String refreshToken) {
		this(idUser, message, token, refreshToken, Instant.now());
	}

	public AuthResponse(String message) {
		this(0, message, "", "", Instant.now());
	}
}
