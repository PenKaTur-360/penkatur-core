package es.taixmiguel.penkatur.core.profiles.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.exceptions.DuplicatedUserException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.tools.log.Log;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class ManageUserTests {

	private final String EMAIL = "t.tester@email.es";
	private final String EMAIL2 = "t.tester.testing@email.es";
	private final String FIRST_NAME = "Test";
	private final String LAST_NAME = "Tester";
	private final String SECOND_LAST_NAME = "Testing";
	private final String AVATAR = "/path/to/image";

	private UserService userService;

	@Autowired
	public ManageUserTests(UserService userService) {
		this.userService = userService;
	}

	@AfterEach
	void deleteUsers() {
		Log.trace(getClass(), "Running user cleanup");
		userService.findUser(EMAIL).ifPresent(u -> userService.deleteUser(u));
		userService.findUser(EMAIL2).ifPresent(u -> userService.deleteUser(u));
	}

	@Test
	void createSimpleUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createSimpleUser1()");
		User user = getInstanceSimpleUser();
		user = userService.createUser(user);
		checkSimpleUser(user);
	}

	@Test
	void createSimpleUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createSimpleUser2()");
		User user = new User(EMAIL, "-", "-");
		user.setCompleteName(FIRST_NAME, LAST_NAME);
		user = userService.createUser(user);
		checkSimpleUser(user);
	}

	@Test
	void createCompleteUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createCompleteUser1()");
		User user = getInstanceCompleteUser();
		user.setAvatar(AVATAR);
		user = userService.createUser(user);
		checkCompleteUser(user);
	}

	@Test
	void createCompleteUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createCompleteUser2()");
		User user = new User(EMAIL, "-", "-");
		user.setAvatar(AVATAR);
		user.setCompleteName(FIRST_NAME, LAST_NAME, SECOND_LAST_NAME);
		user = userService.createUser(user);
		checkCompleteUser(user);
	}

	@Test
	void checkUkUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test checkUkUser1()");
		userService.createUser(getInstanceSimpleUser());
		try {
			userService.createUser(getInstanceCompleteUser());
			fail();
		} catch (DuplicatedUserException e) {
			assertTrue(true);
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	void checkUkUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test checkUkUser2()");
		userService.createUser(getInstanceSimpleUser());
		userService.createUser(getInstanceSimpleUser2());
		try {
			userService.createUser(getInstanceCompleteUser());
			fail();
		} catch (DuplicatedUserException e) {
			assertTrue(true);
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	void findUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test findUser()");
		User user = userService.createUser(getInstanceSimpleUser2());
		assertTrue(userService.findUser(user.getId()).isPresent());
		assertTrue(userService.findUser(user.getEmail()).isPresent());
	}

	@Test
	void updateUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test updateUser()");
		User user = userService.createUser(getInstanceSimpleUser());
		checkSimpleUser(user);
		user.setAvatar(AVATAR);
		user.setCompleteName(FIRST_NAME, LAST_NAME, SECOND_LAST_NAME);
		checkCompleteUser(user);

		User updateUser = userService.updateUser(user);
		checkCompleteUser(updateUser);
	}

	@Test
	void deleteUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test deleteUser()");
		User user = userService.createUser(getInstanceSimpleUser());
		userService.deleteUser(user);

		assertTrue(userService.findUser(user.getId()).isEmpty());
		assertTrue(userService.findUser(user.getEmail()).isEmpty());
	}

	void checkSimpleUser(User user) {
		assertNotNull(user);
		assertNotNull(user.getId());
		assertTrue(user.getId() > 0);
		assertEquals(EMAIL, user.getEmail());
		assertEquals(FIRST_NAME, user.getFirstName());
		assertEquals(LAST_NAME, user.getLastName());
		assertNull(user.getSecondLastName());
		assertNull(user.getAvatar());
		assertNotNull(user.getCreationDate());
	}

	private void checkCompleteUser(User user) {
		assertNotNull(user);
		assertNotNull(user.getId());
		assertTrue(user.getId() > 0);
		assertEquals(EMAIL, user.getEmail());
		assertEquals(FIRST_NAME, user.getFirstName());
		assertEquals(LAST_NAME, user.getLastName());
		assertEquals(SECOND_LAST_NAME, user.getSecondLastName());
		assertEquals(AVATAR, user.getAvatar());
		assertNotNull(user.getCreationDate());
	}

	private User getInstanceSimpleUser() {
		return new User(EMAIL, FIRST_NAME, LAST_NAME);
	}

	private User getInstanceSimpleUser2() {
		return new User(EMAIL2, FIRST_NAME, LAST_NAME);
	}

	private User getInstanceCompleteUser() {
		return new User(EMAIL, FIRST_NAME, LAST_NAME, SECOND_LAST_NAME);
	}
}
