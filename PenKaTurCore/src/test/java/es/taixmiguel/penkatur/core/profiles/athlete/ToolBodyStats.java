package es.taixmiguel.penkatur.core.profiles.athlete;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;

class ToolBodyStats {

	private ToolBodyStats() {
	}

	public static BodyStatsDTO getInstanceSimpleStats() {
		return new BodyStatsDTO(generateRandomDouble(20, 100), Instant.now());
	}

	public static BodyStatsDTO getInstanceCompleteStats() {
		return new BodyStatsDTO(generateRandomDouble(20, 100), generateRandomDouble(20, 80),
				generateRandomDouble(3, 15), generateRandomDouble(5, 40), generateRandomDouble(40, 70),
				generateRandomDouble(5, 30), ThreadLocalRandom.current().nextInt(1000, 2500 + 1),
				generateRandomDouble(20, 60), Instant.now(), null);
	}

	private static double generateRandomDouble(double min, double max) {
		if (min > max)
			throw new IllegalArgumentException("Min must be less than or equal to Max.");
		return ThreadLocalRandom.current().nextDouble(min, max + 1);
	}
}
