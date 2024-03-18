package es.taixmiguel.penkatur.core.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;

@Getter
public class MessageResponse {

	private final String message;
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private final LocalDateTime timestamp;

	public MessageResponse(String message) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
	}
}
