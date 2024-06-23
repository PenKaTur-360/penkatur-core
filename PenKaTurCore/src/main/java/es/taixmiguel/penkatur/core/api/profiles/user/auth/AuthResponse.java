package es.taixmiguel.penkatur.core.api.profiles.user.auth;

public record AuthResponse(long idUser, String message, String token, String refreshToken) {

	public AuthResponse(String message) {
		this(0, message, "", "");
	}
}
