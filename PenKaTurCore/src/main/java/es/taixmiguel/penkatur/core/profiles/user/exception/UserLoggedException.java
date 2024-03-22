package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserLoggedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserLoggedException() {
		super("The user is already logged in.");
	}
}
