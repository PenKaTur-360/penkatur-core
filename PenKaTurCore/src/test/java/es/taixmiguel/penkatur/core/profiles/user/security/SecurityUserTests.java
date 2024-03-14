package es.taixmiguel.penkatur.core.profiles.user.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.profiles.user.ToolUser;
import es.taixmiguel.penkatur.core.profiles.user.exception.DuplicatedUserException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.tools.log.Log;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class SecurityUserTests {

	@Value("${penkatur.profiles.user.security.monthsPasswordExpiration}")
	private int monthsExpiration;

	private UserSecretsService secretsService;
	private UserService userService;

	@Autowired
	public SecurityUserTests(UserService userService, UserSecretsService secretsService) {
		this.secretsService = secretsService;
		this.userService = userService;
	}

	@AfterEach
	void deleteUser() {
		Log.trace(getClass(), "Running user cleanup");
		userService.findUser(ToolUser.EMAIL).ifPresent(u -> userService.deleteUser(u));
	}

	@Test
	void createSecrets0() {
		Log.trace(getClass(), "Running test createSecrets0()");
		UserSecrets secrets = getInstanceSecrets(ToolUser.PASSWORD1, 0);
		checkSecrets(secrets, ToolUser.PASSWORD1, 0);
	}

	@Test
	void changeSecrets0() {
		Log.trace(getClass(), "Running test changeSecrets0()");
		UserSecrets secrets = getInstanceSecrets(ToolUser.PASSWORD1, 0);
		secrets.setPassword(ToolUser.PASSWORD2, 0);
		checkSecrets(secrets, ToolUser.PASSWORD2, 0);
	}

	@Test
	void createSecrets8() {
		Log.trace(getClass(), "Running test createSecrets8()");
		UserSecrets secrets = getInstanceSecrets(ToolUser.PASSWORD1, 8);
		checkSecrets(secrets, ToolUser.PASSWORD1, 8);
	}

	@Test
	void changeSecrets11() {
		Log.trace(getClass(), "Running test changeSecrets11()");
		UserSecrets secrets = getInstanceSecrets(ToolUser.PASSWORD1, 8);
		secrets.setPassword(ToolUser.PASSWORD2, 11);
		checkSecrets(secrets, ToolUser.PASSWORD2, 11);
	}

	@Test
	void saveSecrets() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test saveSecrets()");
		User user = getCreatedUser();
		UserSecrets secrets = secretsService.createSecrets(user, ToolUser.PASSWORD1);
		checkSecrets(secrets, ToolUser.PASSWORD1, monthsExpiration);
	}

	@Test
	void updateSecrets() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test updateSecrets()");
		User user = getCreatedUser();
		UserSecrets secrets = secretsService.createSecrets(user, ToolUser.PASSWORD1);
		secretsService.updatePassword(secrets, ToolUser.PASSWORD2);
		checkSecrets(secrets, ToolUser.PASSWORD2, monthsExpiration);
	}

	private User getCreatedUser() throws DuplicatedUserException {
		return userService.createUser(ToolUser.getInstanceSimpleUser());
	}

	private UserSecrets getInstanceSecrets(String password, int monthsExpiration) {
		User user = ToolUser.getInstanceSimpleUser();
		return new UserSecrets(user, password, monthsExpiration);
	}

	private void checkSecrets(UserSecrets secrets, String password, int monthsExpiration) {
		assertNotNull(secrets, "The secrets instance cannot be null.");
		assertEquals(password, secrets.getPassword(), "The password does not match the expected one.");
		assertEquals(monthsExpiration <= 0 ? LocalDate.MAX : LocalDate.now().plusMonths(monthsExpiration),
				secrets.getPasswordExpiration(), "The password duration does not match the calculated one.");
	}
}
