package es.taixmiguel.penkatur.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApiInfoResponse(@NotBlank String penkaturVersion, @NotNull boolean signupEnabled) {
}