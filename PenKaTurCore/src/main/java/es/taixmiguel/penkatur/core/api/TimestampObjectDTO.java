package es.taixmiguel.penkatur.core.api;

import java.time.Instant;

import es.taixmiguel.penkatur.core.tools.ITimestampObject;
import jakarta.validation.constraints.NotNull;

public record TimestampObjectDTO(@NotNull long id, @NotNull Instant timestamp) {

	public TimestampObjectDTO(ITimestampObject object) {
		this(object.getId(), object.getTimestamp());
	}
}
