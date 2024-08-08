package es.taixmiguel.penkatur.core.api.profiles.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.profiles.user.dto.UserDTO;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserNotFoundException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
public class UserController {

	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> showUser(@PathVariable("id") Long userId) {
		User user = userService.findUser(userId).orElseThrow(UserNotFoundException::new);
		return ResponseEntity.ok(new UserDTO(user));
	}

	@Autowired
	protected void setUserService(UserService service) {
		this.userService = service;
	}
}
