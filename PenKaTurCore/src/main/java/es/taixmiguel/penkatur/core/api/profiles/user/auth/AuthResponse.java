package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public record AuthResponse(long idUser, String message, String token, String refreshToken,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {

	public AuthResponse(long idUser, String message, String token, String refreshToken) {
		this(idUser, message, token, refreshToken, LocalDateTime.now());
	}

	public AuthResponse(String message) {
		this(0, message, "", "", LocalDateTime.now());
	}
}
