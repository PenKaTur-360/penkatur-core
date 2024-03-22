package es.taixmiguel.penkatur.core.profiles.user.security;

import java.time.LocalDate;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USERS_SECRETS")
class UserSecrets {

	@Id
	@Column(name = "ID_USER_SECRETS")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "usersSecretsSeq", sequenceName = "USERS_SECRETS_SEQUENCE", allocationSize = 1)
	private Long id;

	@NotNull
	@OneToOne
	@JoinColumn(name = "ID_USER_FK", foreignKey = @ForeignKey(name = "USERS_SECRETS__USERS__FK"))
	private User user;

	@NotBlank
	private String password;

	@NotNull
	private LocalDate passwordExpiration;

	protected UserSecrets() {
	}

	UserSecrets(@NotNull User user, @NotBlank String password, int monthsExpiration) {
		setPassword(password, monthsExpiration);
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	void setPassword(@NotBlank String password, int monthsExpiration) {
		setPasswordExpiration(monthsExpiration);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public LocalDate getPasswordExpiration() {
		return passwordExpiration;
	}

	public boolean isPasswordExpirated() {
		return passwordExpiration.isBefore(LocalDate.now());
	}

	private void setPasswordExpiration(int monthsExpiration) {
		this.passwordExpiration = monthsExpiration <= 0 ? LocalDate.MAX : LocalDate.now().plusMonths(monthsExpiration);
	}
}
