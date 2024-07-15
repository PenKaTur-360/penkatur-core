package es.taixmiguel.penkatur.core.profiles.athlete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.taixmiguel.penkatur.core.PenKaTurCoreApplication;
import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;
import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import es.taixmiguel.penkatur.core.profiles.athlete.service.BodyStatsService;
import es.taixmiguel.penkatur.core.profiles.user.ToolUser;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.service.UserService;
import es.taixmiguel.penkatur.core.tools.DateTimeUtils;
import es.taixmiguel.penkatur.core.tools.log.Log;

@SpringBootTest(classes = PenKaTurCoreApplication.class)
class BodyStatsTest {

	private BodyStatsService service;
	private UserService userService;

	private User user1;
	private User user2;

	@Autowired
	public BodyStatsTest(BodyStatsService service, UserService userService) {
		this.userService = userService;
		this.service = service;
	}

	@BeforeEach
	void createUsers() {
		Log.trace(getClass(), "Running users create");
		user1 = userService.findUser(ToolUser.EMAIL)
				.orElseGet(() -> userService.createUser(ToolUser.getInstanceSimpleUser()));
		user2 = userService.findUser(ToolUser.EMAIL2)
				.orElseGet(() -> userService.createUser(ToolUser.getInstanceSimpleUser2()));
	}

	@AfterEach
	void deleteUsers() {
		Log.trace(getClass(), "Running users cleanup");
		Arrays.asList(user1, user2).stream().forEach(u -> {
			service.findByUser(u, DateTimeUtils.getMinimumZonedDateTime(), DateTimeUtils.getMaximumZonedDateTime())
					.forEach(service::deleteBodyStats);
			userService.deleteUser(u);
		});
	}

	@Test
	void createSimpleStats1() {
		BodyStats bodyStats = saveAndCheck(user1, ToolBodyStats.getInstanceSimpleStats());
		service.deleteBodyStats(bodyStats);
	}

	@Test
	void createSimpleStats2() {
		BodyStats bodyStats1 = saveAndCheck(user1, ToolBodyStats.getInstanceSimpleStats());
		BodyStats bodyStats2 = saveAndCheck(user2, ToolBodyStats.getInstanceSimpleStats());
		deleteBodyStats(bodyStats1, bodyStats2);
	}

	@Test
	void createCompleteStats1() {
		BodyStats bodyStats = saveAndCheck(user1, ToolBodyStats.getInstanceCompleteStats());
		service.deleteBodyStats(bodyStats);
	}

	@Test
	void createCompleteStats2() {
		BodyStats bodyStats1 = saveAndCheck(user1, ToolBodyStats.getInstanceCompleteStats());
		BodyStats bodyStats2 = saveAndCheck(user2, ToolBodyStats.getInstanceCompleteStats());
		deleteBodyStats(bodyStats1, bodyStats2);
	}

	@Test
	void createCompleteStats3() {
		BodyStatsDTO dto = ToolBodyStats.getInstanceCompleteStats();
		saveAndCheck(user1, dto);

		Optional<BodyStats> bodyStats = service.findNewsByUser(user1, DateTimeUtils.getMinimumZonedDateTime()).stream()
				.findFirst();
		assertNotNull(bodyStats.orElse(null), "The body stats don't was created");
		checkStats(user1, dto, bodyStats.get());
		bodyStats.ifPresent(service::deleteBodyStats);
	}

	@Test
	void checkSimpleStats() {
		ZonedDateTime start = DateTimeUtils.getMinimumZonedDateTime();
		Log.trace(getClass(), "Running test createSimpleStats()");
		BodyStatsDTO stats = ToolBodyStats.getInstanceSimpleStats();
		BodyStats bodyStats = service.createBodyStats(user1, stats);
		assertNotNull(bodyStats, "The body stats don't was created");
		ZonedDateTime end = ZonedDateTime.now();

		List<BodyStats> lStatsUser = service.findByUser(user1, start, end);
		assertEquals(1, lStatsUser.size(), "The body stats don't was retorned");

		List<BodyStats> lStatsNew = service.findNewsByUser(user1, start);
		assertEquals(1, lStatsNew.size(), "The body stats don't was retorned");

		BodyStats bodyStats2 = service.createBodyStats(user1, stats);
		end = ZonedDateTime.now();
		lStatsUser = service.findByUser(user1, start, end);
		assertEquals(2, lStatsUser.size(), "The body stats don't was retorned");

		lStatsNew = service.findNewsByUser(user1, start);
		assertEquals(2, lStatsNew.size(), "The body stats don't was retorned");

		lStatsUser = service.findByUser(user2, start, end);
		assertEquals(0, lStatsUser.size(), "The body stats don't was retorned");

		lStatsNew = service.findNewsByUser(user2, start);
		assertEquals(0, lStatsNew.size(), "The body stats don't was retorned");

		deleteBodyStats(bodyStats, bodyStats2);
		lStatsNew = service.findNewsByUser(user1, start);
		assertEquals(0, lStatsNew.size(), "The body stats don't was deleted");
	}

	private BodyStats saveAndCheck(User user, BodyStatsDTO dto) {
		BodyStats bodyStats = service.createBodyStats(user, dto);
		checkStats(user, dto, bodyStats);
		return bodyStats;
	}

	private void checkStats(User user, BodyStatsDTO dto, BodyStats bodyStats) {
		assertEquals(dto.basalMetabolism(), bodyStats.getBasalMetabolism(), "The basal metabolism is diferent");
		assertEquals(dto.bodyFat(), bodyStats.getBodyFat(), "The body fat is diferent");
		assertEquals(dto.boneMass(), bodyStats.getBoneMass(), "The bone mass is diferent");
		assertEquals(user.getId(), bodyStats.getUser().getId(), "The user is diferent");
		assertEquals(dto.muscle(), bodyStats.getMuscle(), "The muscle is diferent");
		assertEquals(dto.protein(), bodyStats.getProtein(), "The protein is diferent");
		assertEquals(formatDateTime(dto.registerTime()), formatDateTime(bodyStats.getRegisterTime()),
				"The register date is diferent");
		assertEquals(dto.visceralFat(), bodyStats.getVisceralFat(), "The visceral fat is diferent");
		assertEquals(dto.water(), bodyStats.getWater(), "The water is diferent");
		assertEquals(dto.weight(), bodyStats.getWeight(), "The weight is diferent");
	}

	private void deleteBodyStats(BodyStats... bodyStats) {
		Arrays.stream(bodyStats).forEach(stats -> service.deleteBodyStats(stats));
	}

	private String formatDateTime(ZonedDateTime time) {
		return time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss Z"));
	}
}
