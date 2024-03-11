package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import es.taixmiguel.penkatur.core.profiles.user.security.config.UserDetailsImpl;
import es.taixmiguel.penkatur.core.tools.log.Log;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ToolJWT {

	@Value("${penkatur.security.jwt.secret}")
	private String secret;

	@Value("${penkatur.security.jwt.cookieExpiration}")
	private int expiration;

	@Value("${penkatur.security.jwt.cookieName}")
	private String cookieName;

	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		return cookie != null ? cookie.getValue() : null;
	}

	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		return ResponseCookie.from(cookieName, jwt).path("/api").maxAge(expiration).httpOnly(true).build();
	}

	public ResponseCookie getCleanJwtCookie() {
		return ResponseCookie.from(cookieName, null).path("/api").build();
	}

	public String getEmailFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
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

	private String generateTokenFromUsername(String email) {
		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + expiration * 1000))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
