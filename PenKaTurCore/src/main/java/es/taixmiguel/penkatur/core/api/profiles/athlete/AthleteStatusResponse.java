package es.taixmiguel.penkatur.core.api.profiles.athlete;

import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;
import jakarta.validation.constraints.NotNull;

public record AthleteStatusResponse(@NotNull BodyStatsDTO[] bodyStats) {
}
