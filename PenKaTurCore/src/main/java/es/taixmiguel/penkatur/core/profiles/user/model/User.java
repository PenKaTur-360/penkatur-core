package es.taixmiguel.penkatur.core.profiles.user.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserGender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USERS", indexes = @Index(name = "USERS_EMAIL", columnList = "email", unique = true))
public class User {

	@Id
	@Column(name = "ID_USER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "usersSeq", sequenceName = "USERS_SEQUENCE", allocationSize = 1)
	private Long id;

	@Email
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank
	@Size(min = 3)
	private String firstName;

	private String secondName;

	@NotBlank
	@Size(min = 4)
	private String lastName;

	private String secondLastName;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserGender gender;

	@NotNull
	private LocalDate dateOfBirth;

	private String avatar;

	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	protected User() {
	}

	public User(@Email @NotBlank String email, @NotBlank String firstName, @NotBlank String lastName,
			@NotNull UserGender gender, @NotNull LocalDate dateOfBirth) {
		this(email, firstName, null, lastName, null, gender, dateOfBirth);
	}

	public User(@Email @NotBlank String email, @NotBlank String firstName, String secondName, @NotBlank String lastName,
			@NotBlank String secondLastName, @NotNull UserGender gender, @NotNull LocalDate dateOfBirth) {
		setCompleteName(firstName, secondName, lastName, secondLastName);
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setEmail(@Email @NotBlank String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setCompleteName(@NotBlank String firstName, @NotBlank String lastName) {
		setCompleteName(firstName, null, lastName, null);
	}

	public void setCompleteName(@NotBlank String firstName, String secondName, @NotBlank String lastName,
			String secondLastName) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
	}

	public String getCompleteName() {
		return String.format("%s %s %s", getFirstName(), getLastName(), getSecondLastName()).trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName != null ? secondName : "";
	}

	public String getLastName() {
		return lastName;
	}

	public String getSecondLastName() {
		return secondLastName != null ? secondLastName : "";
	}

	public UserGender getGender() {
		return gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		// TODO: if is null or empty, get default avatar
		return avatar;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User aux = (User) obj;
		return Objects.equals(id, aux.id) && Objects.equals(email, aux.email);
	}

	@Override
	public String toString() {
		return String.format("User %s [%d]", getCompleteName(), getId());
	}
}
