package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import org.springframework.http.ResponseCookie;

import es.taixmiguel.penkatur.core.api.MessageResponse;
import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse extends MessageResponse {

	private String jwtRefresh;
	private String jwt;

	public AuthResponse(String message, String jwt, String jwtRefresh) {
		super(message);
		this.jwtRefresh = jwtRefresh;
		this.jwt = jwt;
	}

	public AuthResponse(String message, ResponseCookie jwtCookie, UserToken refreshToken) {
		this(message, jwtCookie.getValue(), refreshToken.getToken());
	}

	public AuthResponse(String message) {
		this(message, "", null);
	}
}
