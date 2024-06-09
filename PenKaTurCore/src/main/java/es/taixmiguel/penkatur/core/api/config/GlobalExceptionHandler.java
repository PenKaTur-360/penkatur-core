package es.taixmiguel.penkatur.core.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import es.taixmiguel.penkatur.core.api.profiles.user.auth.AuthResponse;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserTokenException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserTokenException.class)
	public ResponseEntity<AuthResponse> handleResourceNotFoundException(UserTokenException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponse(ex.getMessage()));
	}
}
