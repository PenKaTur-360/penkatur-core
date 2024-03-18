package es.taixmiguel.penkatur.core.profiles.user;

import java.time.LocalDate;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import es.taixmiguel.penkatur.core.profiles.user.model.User;

public final class ToolUser {

	public static final String EMAIL = "t.tester@email.es";
	public static final String EMAIL2 = "t.tester.testing@email.es";
	public static final String FIRST_NAME = "Test";
	public static final String SECOND_NAME = "Tes";
	public static final String LAST_NAME = "Tester";
	public static final String SECOND_LAST_NAME = "Testing";
	public static final String AVATAR = "/path/to/image";

	private ToolUser() {
	}

	public static User getInstanceSimpleUser() {
		return new User(EMAIL, FIRST_NAME, LAST_NAME, UserGender.MALE, LocalDate.now());
	}

	public static User getInstanceSimpleUser2() {
		return new User(EMAIL2, FIRST_NAME, LAST_NAME, UserGender.FEMALE, LocalDate.now());
	}

	public static User getInstanceCompleteUser() {
		return new User(EMAIL, FIRST_NAME, SECOND_NAME, LAST_NAME, SECOND_LAST_NAME, UserGender.MALE, LocalDate.now());
	}
}
