package es.taixmiguel.penkatur.core.profiles.user.security.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import es.taixmiguel.penkatur.core.tools.log.Log;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil {

	@Value("${penkatur.security.jwt.secret}")
	private String secret;

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(token);
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

	public String getEmail(String token) {
		return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
