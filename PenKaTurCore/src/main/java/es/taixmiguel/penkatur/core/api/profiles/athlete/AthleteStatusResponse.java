package es.taixmiguel.penkatur.core.api.profiles.athlete;

import java.util.List;

import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import jakarta.validation.constraints.NotNull;

public record AthleteStatusResponse(@NotNull List<BodyStats> bodyStats) {
}
