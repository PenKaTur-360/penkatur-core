package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;

import es.taixmiguel.penkatur.core.api.exception.APIException;

public class UserTokenException extends APIException {

	private static final long serialVersionUID = 6895731091394968638L;

	public UserTokenException(HttpStatus status, String message, String errorCode) {
		super(status, message, errorCode);
	}
}
