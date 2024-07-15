package es.taixmiguel.penkatur.core.profiles.user.security;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.MessageResponse;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.AuthResponse;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.RefreshTokenRequest;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.SigninRequest;
import es.taixmiguel.penkatur.core.api.profiles.user.auth.SignupRequest;
import es.taixmiguel.penkatur.core.profiles.user.attributes.UserStatus;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserLoggedException;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserTokenException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import es.taixmiguel.penkatur.core.profiles.user.security.jwt.JwtTokenUtil;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.profiles.user.service.UserTokenService;
import es.taixmiguel.penkatur.core.tools.log.Log;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

	@Value("${penkatur.auth.signup.enabled}")
	private boolean swSignup;

	private JwtTokenUtil jwtTokenUtil;
	private PasswordEncoder encoder;
	private UserService userService;
	private UserSecretsService secretsService;
	private UserTokenService tokenService;
	private AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signup) {
		checkUserNotLogged();
		if (!swSignup)
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: The user registration service is disabled."));
		if (userService.findUser(signup.email()).isPresent())
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));

		User user = signup.toUser();
		user.setStatus(UserStatus.ACTIVE);
		user = userService.createUser(user);
		secretsService.createSecrets(user, encoder.encode(signup.password()));
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody SigninRequest signin) {
		checkUserNotLogged();
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(signin.email(), signin.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return createTokens(userDetails.getId(), "User logged");
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<AuthResponse> refreshtoken(@RequestHeader("Authorization") String authorizationHeader,
			@Valid @RequestBody RefreshTokenRequest request) {
		if (authorizationHeader != null && !authorizationHeader.isBlank()) {
			String accessToken = authorizationHeader.substring(7);
			tokenService.findByAccessToken(accessToken).filter(Predicate.not(UserToken::isTokenExpiringSoon))
					.ifPresent(userToken -> {
						Log.info(getClass(), "A user try refresh his access token");
						throw new UserTokenException(HttpStatus.CONFLICT,
								"Conflict: Token renewal is not allowed at this time as it was recently renewed",
								"TOKEN_NOT_EXPIRED");
					});
		}

		String refreshToken = request.refreshToken();
		if (refreshToken != null && refreshToken.length() > 0)
			return tokenService.findByRefreshToken(refreshToken).filter(token -> {
				tokenService.verifyExpiration(token);
				return true;
			}).map(UserToken::getUser).map(user -> createTokens(user.getId(), "Tokens is refreshed successfully!"))
					.orElseThrow(() -> new UserTokenException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid!",
							"INVALID_REFRESH_TOKEN"));
		return ResponseEntity.badRequest().body(new AuthResponse("Refresh token is empty!"));
	}

	private ResponseEntity<AuthResponse> createTokens(Long userId, String msg) {
		UserToken accessToken = tokenService.createOrUpdateAccessToken(userId, jwtTokenUtil);
		UserToken refreshToken = tokenService.updateOrCreateRefreshToken(userId);
		return ResponseEntity.ok().body(new AuthResponse(userId, msg, accessToken, refreshToken));
	}

	@PostMapping("/signout")
	public ResponseEntity<MessageResponse> logoutUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!"anonymousUser".equals(principal.toString())) {
			Long userId = ((UserDetailsImpl) principal).getId();
			tokenService.deleteByUser(userId);
		}

		return ResponseEntity.ok().body(new MessageResponse("You've been signed out!"));
	}

	private void checkUserNotLogged() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!"anonymousUser".equals(principal.toString()))
			throw new UserLoggedException();
	}

	@Autowired
	protected void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
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
	protected void setTokenService(UserTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Autowired
	protected void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
