package es.taixmiguel.penkatur.core.api.profiles.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User")
@RequestMapping("/api/user")
public class ApiUserStatusController {

	@GetMapping("/status")
	public ResponseEntity<UserStatusResponse> showStatus() {
		return ResponseEntity.ok(new UserStatusResponse(false));
	}
}
