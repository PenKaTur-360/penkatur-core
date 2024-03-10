package es.taixmiguel.penkatur.core.exceptions;

public class DuplicatedUserException extends Exception {

	private static final long serialVersionUID = -7660941317225544633L;

	public DuplicatedUserException() {
		super("The email address provided already exists in the system.");
	}
}
