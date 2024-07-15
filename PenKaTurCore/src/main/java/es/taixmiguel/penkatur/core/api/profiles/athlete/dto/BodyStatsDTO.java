package es.taixmiguel.penkatur.core.api.profiles.athlete.dto;

import java.time.ZonedDateTime;

import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import jakarta.validation.constraints.NotNull;

public record BodyStatsDTO(long id, @NotNull double weight, double muscle, double visceralFat, double bodyFat,
		double water, double protein, int basalMetabolism, double boneMass, @NotNull ZonedDateTime registerTime,
		ZonedDateTime timestamp) {

	public BodyStatsDTO(@NotNull double weight, double muscle, double visceralFat, double bodyFat, double water,
			double protein, int basalMetabolism, double boneMass, @NotNull ZonedDateTime registerTime,
			ZonedDateTime timestamp) {
		this(0, weight, muscle, visceralFat, bodyFat, water, protein, basalMetabolism, boneMass, registerTime,
				timestamp);
	}

	public BodyStatsDTO(@NotNull double weight, @NotNull ZonedDateTime registerTime) {
		this(0, weight, 0, 0, 0, 0, 0, 0, 0, registerTime, null);
	}

	public BodyStatsDTO(BodyStats bodyStats) {
		this(bodyStats.getId(), bodyStats.getWeight(), bodyStats.getMuscle(), bodyStats.getVisceralFat(),
				bodyStats.getBodyFat(), bodyStats.getWater(), bodyStats.getProtein(), bodyStats.getBasalMetabolism(),
				bodyStats.getBoneMass(), bodyStats.getRegisterTime(), bodyStats.getTimestamp());
	}
}
