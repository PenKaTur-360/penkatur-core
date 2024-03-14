package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicatedUserException() {
		super("The email address provided already exists in the system.");
	}
}
