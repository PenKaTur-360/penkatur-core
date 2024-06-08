package es.taixmiguel.penkatur.core.profiles.user.model;

import java.time.Instant;
import java.util.UUID;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserTokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TOKENS", uniqueConstraints = {
		@UniqueConstraint(name = "UK_User_Type", columnNames = { "user", "type" }),
		@UniqueConstraint(name = "UK_Type_Token", columnNames = { "type", "token" }) })
public class UserToken {

	@Id
	@Column(name = "ID_TOKEN")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "tokenSeq", sequenceName = "TOKENS_SEQUENCE", allocationSize = 1)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "ID_USER_FK", foreignKey = @ForeignKey(name = "TOKENS__USERS__FK"))
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserTokenType type;

	@NotBlank
	private String token;

	@NotNull
	private Instant expiryDate;

	protected UserToken() {
	}

	public UserToken(@NotNull User user, @NotNull UserTokenType type, @NotNull Long tokenDuration) {
		this(user, type, tokenDuration, null);
	}

	public UserToken(@NotNull User user, @NotNull UserTokenType type, @NotNull Long tokenDuration, String token) {
		setToken(tokenDuration, token);
		this.type = type;
		this.user = user;
	}

	public UserToken regenerateToken(@NotNull Long tokenDuration) {
		return setToken(tokenDuration, null);
	}

	public UserToken setToken(@NotNull Long tokenDuration, String token) {
		this.token = token != null && !token.isBlank() ? token : UUID.randomUUID().toString();
		this.expiryDate = Instant.now().plusMillis(tokenDuration * 1000);
		return this;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public UserTokenType getType() {
		return type;
	}

	public String getToken() {
		return token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public boolean hasExpired() {
		return Instant.now().isAfter(getExpiryDate());
	}
}
