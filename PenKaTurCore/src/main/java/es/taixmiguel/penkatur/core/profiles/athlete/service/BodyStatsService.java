package es.taixmiguel.penkatur.core.profiles.athlete.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;
import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import es.taixmiguel.penkatur.core.profiles.athlete.repository.BodyStatsRepository;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.tools.log.Log;
import es.taixmiguel.penkatur.core.tools.service.AbstractPenkaturService;
import jakarta.validation.constraints.NotNull;

@Service
public class BodyStatsService extends AbstractPenkaturService {

	private BodyStatsRepository repo;

	public BodyStats createBodyStats(@NotNull long userId, @NotNull BodyStatsDTO dto) {
		return createBodyStats(findUser(userId), dto);
	}

	public BodyStats createBodyStats(@NotNull User user, @NotNull BodyStatsDTO dto) {
		try {
			BodyStats bodyStats = repo.save(toBodyStats(user, dto));
			Log.debug(getClass(), "A body stats created for the user %s at date %s", bodyStats.getUser().toString(),
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault())
							.format(bodyStats.getRegisterTime()));
			return bodyStats;
		} catch (Exception e) {
			Log.error(getClass(), "Error in createBodyStats(BodyStatsDTO)", e);
			throw e;
		}
	}

	public List<BodyStats> findNewsByUser(@NotNull long userId, @NotNull ZonedDateTime instant) {
		return findNewsByUser(findUser(userId), instant);
	}

	public List<BodyStats> findNewsByUser(@NotNull User user, @NotNull ZonedDateTime instant) {
		try {
			return repo.findByUserAndTimestampAfter(user, instant);
		} catch (Exception e) {
			Log.error(getClass(), "Error in findNewsByUser(User, ZonedDateTime)", e);
			throw e;
		}
	}

	public List<BodyStats> findByUser(@NotNull long userId, @NotNull ZonedDateTime startDate,
			@NotNull ZonedDateTime endDate) {
		return findByUser(findUser(userId), startDate, endDate);
	}

	public List<BodyStats> findByUser(@NotNull User user, @NotNull ZonedDateTime startTime,
			@NotNull ZonedDateTime endTime) {
		if (startTime.isAfter(endTime))
			throw new IllegalArgumentException("Start date must be before end date");

		try {
			return repo.findAllByUserAndRegisterTimeBetween(user, startTime, endTime);
		} catch (Exception e) {
			Log.error(getClass(), "Error in findByUser(User, Instant, Instant)", e);
			throw e;
		}
	}

	public void deleteBodyStats(@NotNull BodyStats bodyStats) {
		try {
			repo.delete(bodyStats);
			Log.debug(getClass(), "The body stats with id %d has been deleted", bodyStats.getId());
		} catch (Exception e) {
			Log.error(getClass(), "Error in deleteBodyStats(BodyStats)", e);
			throw e;
		}
	}

	@Autowired
	public void setRepository(BodyStatsRepository repository) {
		this.repo = repository;
	}

	private BodyStats toBodyStats(User user, BodyStatsDTO dto) {
		BodyStats bodyStats = new BodyStats(user, dto.weight(), dto.registerTime());
		bodyStats.setWeight(dto.weight());
		bodyStats.setMuscle(dto.muscle());
		bodyStats.setVisceralFat(dto.visceralFat());
		bodyStats.setBodyFat(dto.bodyFat());
		bodyStats.setWater(dto.water());
		bodyStats.setProtein(dto.protein());
		bodyStats.setBasalMetabolism(dto.basalMetabolism());
		bodyStats.setBoneMass(dto.boneMass());
		return bodyStats;
	}
}
