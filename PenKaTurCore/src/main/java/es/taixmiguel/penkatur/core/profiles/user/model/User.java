package es.taixmiguel.penkatur.core.profiles.user.model;

import java.util.Date;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@NotBlank
	@Column(unique = true)
	private String email;

	@NotBlank
	@Size(min = 3)
	private String firstName;

	@NotBlank
	@Size(min = 4)
	private String lastName;

	private String secondLastName;

	private String avatar;

	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	protected User() {
	}

	public User(@Email @NotBlank String email, @NotBlank String firstName, @NotBlank String lastName) {
		this(email, firstName, lastName, null);
	}

	public User(@Email @NotBlank String email, @NotBlank String firstName, @NotBlank String lastName,
			@NotBlank String secondLastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
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
		setCompleteName(firstName, lastName, null);
	}

	public void setCompleteName(@NotBlank String firstName, @NotBlank String lastName, String secondLastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
	}

	public String getCompleteName() {
		return String.format("%s %s %s", getFirstName(), getLastName(), getSecondLastName()).trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSecondLastName() {
		return secondLastName != null ? secondLastName : "";
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
