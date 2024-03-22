package es.taixmiguel.penkatur.core.profiles.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.MessageResponse;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.SigninRequest;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.SignupRequest;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserLoggedException;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserTokenException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import es.taixmiguel.penkatur.core.profiles.user.security.jwt.ToolJWT;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.profiles.user.service.UserTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

	private ToolJWT jwtUtils;
	private PasswordEncoder encoder;
	private UserService userService;
	private UserSecretsService secretsService;
	private UserTokenService refreshTokenService;
	private AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signup) {
		checkUserNotLogged();
		if (userService.findUser(signup.getEmail()).isPresent())
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));

		User user = userService.createUser(signup.toUser());
		secretsService.createSecrets(user, encoder.encode(signup.getPassword()));
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signin")
	public ResponseEntity<MessageResponse> authenticateUser(@Valid @RequestBody SigninRequest signin) {
		checkUserNotLogged();
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		UserToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString()).body(new MessageResponse("User logged"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<MessageResponse> refreshtoken(HttpServletRequest request) {
		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			return refreshTokenService.findByRefreshToken(refreshToken).map(refreshTokenService::verifyExpiration)
					.map(UserToken::getUser).map(user -> {
						ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
						return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(new MessageResponse("Token is refreshed successfully!"));
					}).orElseThrow(() -> new UserTokenException("Refresh token is not in database!"));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is empty!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<MessageResponse> logoutUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!"anonymousUser".equals(principal.toString())) {
			Long userId = ((UserDetailsImpl) principal).getId();
			refreshTokenService.deleteByUser(userId);
		}

		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}

	private void checkUserNotLogged() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!"anonymousUser".equals(principal.toString()))
			throw new UserLoggedException();
	}

	@Autowired
	protected void setJwtUtils(ToolJWT jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Autowired
	protected void setEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	@Autowired
	protected void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	protected void setSecretsService(UserSecretsService secretsService) {
		this.secretsService = secretsService;
	}

	@Autowired
	protected void setRefreshTokenService(UserTokenService refreshTokenService) {
		this.refreshTokenService = refreshTokenService;
	}

	@Autowired
	protected void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
