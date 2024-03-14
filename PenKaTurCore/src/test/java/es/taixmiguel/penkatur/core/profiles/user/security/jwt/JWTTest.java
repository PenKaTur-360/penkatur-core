package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.profiles.user.ToolUser;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class JWTTest {

	private ToolJWT toolJWT;

	@Autowired
	public JWTTest(ToolJWT toolJWT) {
		this.toolJWT = toolJWT;
	}

	@Test
	void checkToken() {
		String token = toolJWT.generateTokenFromEmail(ToolUser.EMAIL);
		assertNotEquals(ToolUser.EMAIL, token, "The token is same that email");
		assertEquals(ToolUser.EMAIL, toolJWT.getEmailFromJwtToken(token), "The email from token isn't same that email");
	}

	@Test
	void validateToken() {
		String token = toolJWT.generateTokenFromEmail(ToolUser.EMAIL);

		try {
			toolJWT.validateJwtToken(token);
			assertTrue(true);
		} catch (Exception e) {
			fail("Token isn't valid");
		}
	}
}
