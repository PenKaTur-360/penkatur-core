package es.taixmiguel.penkatur.core.api.profiles.user.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SigninRequest(@Email @NotBlank String email, @NotBlank String password) {
}