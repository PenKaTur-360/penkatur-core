package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserTokenException(String message) {
		super(message);
	}
}
