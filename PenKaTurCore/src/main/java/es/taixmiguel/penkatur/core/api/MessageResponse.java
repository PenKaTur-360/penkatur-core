package es.taixmiguel.penkatur.core.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public record MessageResponse(String message,
		@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime timestamp) {

	public MessageResponse(String message) {
		this(message, LocalDateTime.now());
	}
}
