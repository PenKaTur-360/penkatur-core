package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.security.UserDetailsImpl;
import es.taixmiguel.penkatur.core.tools.log.Log;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ToolJWT {

	@Value("${penkatur.security.jwt.cookieExpiration}")
	private int expiration;

	@Value("${penkatur.security.jwt.cookieName}")
	private String cookieName;

	@Value("${penkatur.security.jwt.cookieRefreshName}")
	private String cookieRefreshName;

	@Value("${penkatur.security.jwt.cookieRefreshExpiration}")
	private int refreshTokenDurationJWT;

	@Value("${penkatur.security.jwt.secret}")
	private String secret;

	public ResponseCookie generateJwtCookie(UserDetailsImpl user) {
		return generateJwtCookie(user.getUsername());
	}

	public ResponseCookie generateJwtCookie(User user) {
		return generateJwtCookie(user.getEmail());
	}

	public String getJwtFromCookies(HttpServletRequest request) {
		return getValueCookie(request, cookieName);
	}

	public ResponseCookie getCleanJwtCookie() {
		return generateNullCookie(cookieName, "/api");
	}

	public ResponseCookie generateRefreshJwtCookie(String token) {
		return generateCookie(cookieRefreshName, token, "/api/auth/refreshtoken", refreshTokenDurationJWT);
	}

	public String getJwtRefreshFromCookies(HttpServletRequest request) {
		return getValueCookie(request, cookieRefreshName);
	}

	public ResponseCookie getCleanJwtRefreshCookie() {
		return generateNullCookie(cookieRefreshName, "/api/auth/refreshtoken");
	}

	public String getEmailFromJwtToken(String token) {
		return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			Log.error(getClass(), "Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			Log.error(getClass(), "JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			Log.error(getClass(), "JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.error(getClass(), "JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromEmail(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + expiration * 1000)).signWith(key(), Jwts.SIG.HS256)
				.compact();
	}

	private ResponseCookie generateJwtCookie(String email) {
		String jwt = generateTokenFromEmail(email);
		return generateCookie(cookieName, jwt, "/api", expiration);
	}

	private ResponseCookie generateCookie(String cookieName, String cookieValue, String path, int expiration) {
		return ResponseCookie.from(cookieName, cookieValue).path(path).maxAge(expiration).httpOnly(true).build();
	}

	private ResponseCookie generateNullCookie(String cookie, String path) {
		return ResponseCookie.from(cookie, null).path(path).build();
	}

	public String getValueCookie(HttpServletRequest request, String cookieName) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		return cookie != null ? cookie.getValue() : null;
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
