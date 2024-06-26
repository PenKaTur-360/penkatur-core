package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;

import es.taixmiguel.penkatur.core.api.exception.APIException;

public class DuplicatedUserException extends APIException {

	private static final long serialVersionUID = 6798458981347785441L;

	public DuplicatedUserException() {
		super(HttpStatus.CONFLICT, "The email address provided already exists in the system.", "EMAIL_IN_USE");
	}
}
