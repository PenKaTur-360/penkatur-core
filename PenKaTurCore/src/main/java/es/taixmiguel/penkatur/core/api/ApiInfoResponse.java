package es.taixmiguel.penkatur.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiInfoResponse {

	private @NotBlank String penkaturVersion;
	private @NotNull boolean signupEnabled;
}
