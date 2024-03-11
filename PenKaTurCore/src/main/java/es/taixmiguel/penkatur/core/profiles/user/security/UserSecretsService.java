package es.taixmiguel.penkatur.core.profiles.user.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
public class UserSecretsService {

	@Value("${penkatur.profiles.user.security.monthsPasswordExpiration}")
	private int monthsExpiration;

	private UserSecretsRepository secretsRepo;

	UserSecrets createSecrets(@NotNull User user, @NotBlank String password) {
		return secretsRepo.save(new UserSecrets(user, password, monthsExpiration));
	}

	Optional<UserSecrets> findSecrets(User user) {
		return secretsRepo.findByUser(user);
	}

	UserSecrets updatePassword(@NotNull UserSecrets secrets, @NotBlank String password) {
		secrets.setPassword(password, monthsExpiration);
		return secretsRepo.saveAndFlush(secrets);
	}

	public void deleteSecrets(User user) {
		Optional<UserSecrets> secrets = findSecrets(user);
		secrets.ifPresent(s -> secretsRepo.delete(s));
	}

	@Autowired
	public void setUserSecretsRepository(UserSecretsRepository secretsRepo) {
		this.secretsRepo = secretsRepo;
	}
}
