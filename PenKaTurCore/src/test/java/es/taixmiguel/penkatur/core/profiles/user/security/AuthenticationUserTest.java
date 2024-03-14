package es.taixmiguel.penkatur.core.profiles.user.security;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.profiles.user.ToolUser;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class AuthenticationUserTest {

	private AuthenticationManager authenticationManager;
	private UserSecretsService secretsService;
	private UserService userService;
	private PasswordEncoder encoder;

	@Autowired
	public AuthenticationUserTest(AuthenticationManager authenticationManager, UserSecretsService secretsService,
			UserService userService, PasswordEncoder encoder) {
		this.authenticationManager = authenticationManager;
		this.secretsService = secretsService;
		this.userService = userService;
		this.encoder = encoder;
	}

	@Test
	void authenticateUser() {
		User user = userService.createUser(ToolUser.getInstanceSimpleUser());
		secretsService.createSecrets(user, encoder.encode(ToolUser.PASSWORD1));

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), ToolUser.PASSWORD1));
			assertTrue(true);
		} catch (Exception e) {
			fail("The user can't be authenticate");
		} finally {
			userService.deleteUser(user);
		}
	}
}
