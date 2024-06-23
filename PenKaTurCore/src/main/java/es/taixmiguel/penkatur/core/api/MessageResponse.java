package es.taixmiguel.penkatur.core.api;

import java.time.Instant;

public record MessageResponse(String message, Instant timestamp) {

	public MessageResponse(String message) {
		this(message, Instant.now());
	}
}
