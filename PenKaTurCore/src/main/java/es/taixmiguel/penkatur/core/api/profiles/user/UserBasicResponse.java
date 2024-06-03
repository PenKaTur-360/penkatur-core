package es.taixmiguel.penkatur.core.api.profiles.user;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserBasicResponse {

	private String avatar;
	private @NotNull long idUser;
	private @NotBlank String completeName;

	public UserBasicResponse(User user) {
		this.idUser = user.getId();
		this.avatar = user.getAvatar();
		this.completeName = user.getCompleteName();
	}
}
