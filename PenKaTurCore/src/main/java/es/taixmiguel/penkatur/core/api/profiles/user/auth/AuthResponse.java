package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AuthResponse(String message, String jwt, String jwtRefresh,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {

	public AuthResponse(String message, String jwt, String jwtRefresh) {
		this(message, jwt, jwtRefresh, LocalDateTime.now());
	}
}
