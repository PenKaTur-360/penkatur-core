package es.taixmiguel.penkatur.core.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Information")
public class ApiInfoController {

	@Value("${penkatur.app.version}")
	private String penkaturVersion;
	@Value("${penkatur.auth.signup.enabled}")
	private boolean signupEnabled;

	@GetMapping("/info")
	public ResponseEntity<ApiInfoResponse> showInformation() {
		ApiInfoResponse infoResponse = new ApiInfoResponse(penkaturVersion, signupEnabled);
		return ResponseEntity.ok(infoResponse);
	}
}
