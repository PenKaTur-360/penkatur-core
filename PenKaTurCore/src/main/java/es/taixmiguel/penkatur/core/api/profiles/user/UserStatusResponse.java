package es.taixmiguel.penkatur.core.api.profiles.user;

import jakarta.validation.constraints.NotNull;

public record UserStatusResponse(@NotNull boolean swChanges) {
}
