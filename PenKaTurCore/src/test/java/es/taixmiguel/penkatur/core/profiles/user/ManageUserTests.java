package es.taixmiguel.penkatur.core.profiles.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import es.taixmiguel.penkatur.core.profiles.user.exception.DuplicatedUserException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.tools.log.Log;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class ManageUserTests {

	private UserService userService;

	@Autowired
	public ManageUserTests(UserService userService) {
		this.userService = userService;
	}

	@AfterEach
	void deleteUsers() {
		Log.trace(getClass(), "Running user cleanup");
		userService.findUser(ToolUser.EMAIL).ifPresent(u -> userService.deleteUser(u));
		userService.findUser(ToolUser.EMAIL2).ifPresent(u -> userService.deleteUser(u));
	}

	@Test
	void createSimpleUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createSimpleUser1()");
		User user = ToolUser.getInstanceSimpleUser();
		user = userService.createUser(user);
		checkSimpleUser(user);
	}

	@Test
	void createSimpleUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createSimpleUser2()");
		User user = new User(ToolUser.EMAIL, "-", "-", UserGender.MALE, LocalDate.now());
		user.setCompleteName(ToolUser.FIRST_NAME, ToolUser.LAST_NAME);
		user = userService.createUser(user);
		checkSimpleUser(user);
	}

	@Test
	void createCompleteUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createCompleteUser1()");
		User user = ToolUser.getInstanceCompleteUser();
		user.setAvatar(ToolUser.AVATAR);
		user = userService.createUser(user);
		checkCompleteUser(user);
	}

	@Test
	void createCompleteUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test createCompleteUser2()");
		User user = new User(ToolUser.EMAIL, "-", "-", UserGender.MALE, LocalDate.now());
		user.setAvatar(ToolUser.AVATAR);
		user.setCompleteName(ToolUser.FIRST_NAME, ToolUser.SECOND_NAME, ToolUser.LAST_NAME, ToolUser.SECOND_LAST_NAME);
		user = userService.createUser(user);
		checkCompleteUser(user);
	}

	@Test
	void checkUkUser1() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test checkUkUser1()");
		userService.createUser(ToolUser.getInstanceSimpleUser());
		try {
			userService.createUser(ToolUser.getInstanceCompleteUser());
			fail("The user was not supposed to be created");
		} catch (DuplicatedUserException e) {
			assertTrue(true);
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	void checkUkUser2() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test checkUkUser2()");
		userService.createUser(ToolUser.getInstanceSimpleUser());
		userService.createUser(ToolUser.getInstanceSimpleUser2());
		try {
			userService.createUser(ToolUser.getInstanceCompleteUser());
			fail("The user was not supposed to be created");
		} catch (DuplicatedUserException e) {
			assertTrue(true);
		} catch (Throwable t) {
			fail(t.getMessage());
		}
	}

	@Test
	void findUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test findUser()");
		User user = userService.createUser(ToolUser.getInstanceSimpleUser2());
		assertTrue(userService.findUser(user.getId()).isPresent(), "The user was found by searching for ID");
		assertTrue(userService.findUser(user.getEmail()).isPresent(), "The user was found by searching for email");
	}

	@Test
	void updateUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test updateUser()");
		User user = userService.createUser(ToolUser.getInstanceSimpleUser());
		checkSimpleUser(user);
		user.setAvatar(ToolUser.AVATAR);
		user.setCompleteName(ToolUser.FIRST_NAME, ToolUser.SECOND_NAME, ToolUser.LAST_NAME, ToolUser.SECOND_LAST_NAME);
		checkCompleteUser(user);

		User updateUser = userService.updateUser(user);
		checkCompleteUser(updateUser);
	}

	@Test
	void deleteUser() throws DuplicatedUserException {
		Log.trace(getClass(), "Running test deleteUser()");
		User user = userService.createUser(ToolUser.getInstanceSimpleUser());
		userService.deleteUser(user);

		assertTrue(userService.findUser(user.getId()).isEmpty(), "The user was found by searching for ID");
		assertTrue(userService.findUser(user.getEmail()).isEmpty(), "The user was found by searching for email");
	}

	private void checkCommonUser(User user) {
		assertNotNull(user, "The user should not be null");
		assertNotNull(user.getId(), "The user has no identifier");
		assertTrue(user.getId() > 0, "The user identifier is not greater than 0");
		assertEquals(ToolUser.EMAIL, user.getEmail(), "The user does not have the expected email");
		assertEquals(ToolUser.FIRST_NAME, user.getFirstName(), "The user does not have the expected name");
		assertEquals(ToolUser.LAST_NAME, user.getLastName(), "The user does not have the expected last name");
		assertNotNull(user.getCreationDate(), "The user does not have creation date");

	}

	private void checkSimpleUser(User user) {
		checkCommonUser(user);
		assertNull(user.getAvatar(), "The user does not have the expected avatar");
		assertEquals("", user.getSecondLastName(), "The user does not have the expected second last name");
	}

	private void checkCompleteUser(User user) {
		checkCommonUser(user);
		assertEquals(ToolUser.AVATAR, user.getAvatar(), "The user does not have the expected avatar");
		assertEquals(ToolUser.SECOND_NAME, user.getSecondName(), "The user does not have the expected second name");
		assertEquals(ToolUser.SECOND_LAST_NAME, user.getSecondLastName(),
				"The user does not have the expected second last name");
	}
}
