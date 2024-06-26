package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;

import es.taixmiguel.penkatur.core.api.exception.APIException;

public class UserLoggedException extends APIException {

	private static final long serialVersionUID = -8593820875155915343L;

	public UserLoggedException() {
		super(HttpStatus.CONFLICT, "The user is already logged in.", "USER_LOGGED");
	}
}
