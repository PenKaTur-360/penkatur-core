package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public record AuthResponse(String message, String token, String refreshToken,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {

	public AuthResponse(String message, String token, String refreshToken) {
		this(message, token, refreshToken, LocalDateTime.now());
	}

	public AuthResponse(String message) {
		this(message, "", "", LocalDateTime.now());
	}
}
