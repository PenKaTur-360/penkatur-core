package es.taixmiguel.penkatur.core.api.exception;

import org.springframework.http.HttpStatus;

public abstract class APIException extends RuntimeException {

	private static final long serialVersionUID = -3678405470227698596L;

	private final HttpStatus status;
	private final String errorCode;

	protected APIException(HttpStatus status, String message, String errorCode) {
		super(message);
		this.status = status;
		this.errorCode = errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
