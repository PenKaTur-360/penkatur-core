package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;

public record AuthResponse(String message, String jwt, String jwtRefresh,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {

	public AuthResponse(String message, ResponseCookie jwtCookie, UserToken refreshToken) {
		this(message, jwtCookie.getValue(), refreshToken.getToken(), LocalDateTime.now());
	}

	public AuthResponse(String message) {
		this(message, "", "", LocalDateTime.now());
	}
}
