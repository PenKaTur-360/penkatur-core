package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {
}
