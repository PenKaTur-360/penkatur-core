package es.taixmiguel.penkatur.core.api.profiles.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.profiles.user.UserStatusResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
public class UserStatusController {

	@GetMapping("/{id}/status")
	public ResponseEntity<UserStatusResponse> showStatus(@PathVariable("id") Long userId) {
		return ResponseEntity.ok(new UserStatusResponse(false));
	}
}
