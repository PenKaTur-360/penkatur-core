package es.taixmiguel.penkatur.core.profiles.user.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserTokenType;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserNotFoundException;
import es.taixmiguel.penkatur.core.profiles.user.exception.UserTokenException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import es.taixmiguel.penkatur.core.profiles.user.repository.UserTokenRepository;
import jakarta.transaction.Transactional;

@Service
public class UserTokenService {

	@Value("${penkatur.security.jwt.cookieRefreshExpiration}")
	private int refreshTokenDurationJWT;

	private UserService userService;
	private UserTokenRepository tokenRepo;

	public Optional<UserToken> findByRefreshToken(String token) {
		return tokenRepo.findByTypeAndToken(UserTokenType.REFRESH, token);
	}

	public UserToken createRefreshToken(long idUser) {
		return createToken(UserTokenType.REFRESH, getUser(idUser), refreshTokenDurationJWT);
	}

	private UserToken createToken(UserTokenType type, User user, long tokenDuration) {
		String token = UUID.randomUUID().toString();
		Instant expiration = Instant.now().plusMillis(tokenDuration);
		return tokenRepo.save(new UserToken(user, type, token, expiration));
	}

	public UserToken verifyExpiration(UserToken token) {
		if (token.hasExpired()) {
			tokenRepo.delete(token);
			throw new UserTokenException("Token was expired");
		}
		return token;
	}

	@Transactional
	public int deleteByUser(long idUser) {
		return tokenRepo.deleteByUser(getUser(idUser));
	}

	private User getUser(long idUser) {
		return userService.findUser(idUser).orElseThrow(() -> new UserNotFoundException(idUser));
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setTokenRepo(UserTokenRepository tokenRepo) {
		this.tokenRepo = tokenRepo;
	}
}
