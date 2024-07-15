package es.taixmiguel.penkatur.core.profiles.athlete.model;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.tools.ITimestampObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "BODY_STATS", indexes = { @Index(columnList = "registerTime", name = "BODY_STATS__REGISTER_TIME"),
		@Index(columnList = "timestamp", name = "BODY_STATS__TIMESTAMP") })
public class BodyStats implements ITimestampObject {

	@Id
	@Column(name = "ID_BODY_STATS")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "bodyStatsSeq", sequenceName = "BODY_STATS_SEQUENCE", allocationSize = 1)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "ID_USER_FK", foreignKey = @ForeignKey(name = "BODY_STATS__USERS__FK"))
	private User user;

	@NotNull
	private double weight;

	private double muscle;

	private double visceralFat;

	private double bodyFat;

	private double water;

	private double protein;

	private int basalMetabolism;

	private double boneMass;

	@NotNull
	private ZonedDateTime registerTime;

	@NotNull
	private ZonedDateTime timestamp;

	protected BodyStats() {
	}

	public BodyStats(@NotNull User user, @NotNull double weight) {
		this(user, weight, ZonedDateTime.now());
	}

	public BodyStats(@NotNull User user, @NotNull double weight, @NotNull ZonedDateTime registerTime) {
		this.registerTime = registerTime;
		this.weight = weight;
		this.user = user;
	}

	@Override
	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getMuscle() {
		return muscle;
	}

	public void setMuscle(double muscle) {
		this.muscle = muscle;
	}

	public double getVisceralFat() {
		return visceralFat;
	}

	public void setVisceralFat(double visceralFat) {
		this.visceralFat = visceralFat;
	}

	public double getBodyFat() {
		return bodyFat;
	}

	public void setBodyFat(double bodyFat) {
		this.bodyFat = bodyFat;
	}

	public double getWater() {
		return water;
	}

	public void setWater(double water) {
		this.water = water;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public int getBasalMetabolism() {
		return basalMetabolism;
	}

	public void setBasalMetabolism(int basalMetabolism) {
		this.basalMetabolism = basalMetabolism;
	}

	public double getBoneMass() {
		return boneMass;
	}

	public void setBoneMass(double boneMass) {
		this.boneMass = boneMass;
	}

	public ZonedDateTime getRegisterTime() {
		return registerTime;
	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, registerTime, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BodyStats other = (BodyStats) obj;
		return Objects.equals(id, other.id) && Objects.equals(registerTime, other.registerTime)
				&& Objects.equals(user, other.user);
	}

	@PrePersist
	protected void onCreate() {
		timestamp = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@PreUpdate
	protected void onUpdate() {
		timestamp = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}
}
