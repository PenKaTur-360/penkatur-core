package es.taixmiguel.penkatur.core.profiles.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(long idUser) {
		super(String.format("the user with id %d not exists", idUser));
	}
}
