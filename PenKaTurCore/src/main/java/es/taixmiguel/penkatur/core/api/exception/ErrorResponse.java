package es.taixmiguel.penkatur.core.api.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int status, String message, String errorCode) {

	public ErrorResponse(HttpStatus status, String message, String errorCode) {
		this(status.value(), message, errorCode);
	}

	public ErrorResponse(APIException exception) {
		this(exception.getStatus(), exception.getMessage(), exception.getErrorCode());
	}
}
