package es.taixmiguel.penkatur.core.tools;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeUtils {

	private DateTimeUtils() {
	}

	public static ZonedDateTime getMinimumZonedDateTime() {
		return ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
	}

	public static ZonedDateTime getMaximumZonedDateTime() {
		return ZonedDateTime.of(2099, 12, 31, 23, 59, 59, 99, ZoneId.systemDefault());
	}
}
