package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;

import es.taixmiguel.penkatur.core.api.exception.APIException;

public class UserNotFoundException extends APIException {

	private static final long serialVersionUID = -6299212259192109572L;

	public UserNotFoundException(long idUser) {
		super(HttpStatus.NOT_FOUND, String.format("The user with id %d not exists", idUser), "USER_NOT_EXISTS");
	}

	public UserNotFoundException() {
		super(HttpStatus.NOT_FOUND, "The user not exists", "USER_NOT_EXISTS");
	}
}
