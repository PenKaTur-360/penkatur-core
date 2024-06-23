package es.taixmiguel.penkatur.core.tools.service;

import org.springframework.beans.factory.annotation.Autowired;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import jakarta.validation.constraints.NotNull;

public abstract class AbstractPenkaturService {

	private UserService userService;

	protected User findUser(@NotNull long userId) {
		return userService.findUser(userId).orElseThrow();
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
