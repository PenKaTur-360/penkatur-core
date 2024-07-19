package es.taixmiguel.penkatur.core.api.profiles.athlete.controller;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.profiles.athlete.AthleteStatusResponse;
import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;
import es.taixmiguel.penkatur.core.profiles.athlete.service.BodyStatsService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Athlete")
@RequestMapping("/api/athlete")
public class AthleteController {

	private ApplicationContext applicationContext;

	@GetMapping("/{id}/status")
	public ResponseEntity<AthleteStatusResponse> showStatus(@PathVariable("id") Long athleteId,
			@RequestParam ZonedDateTime checkDate) {
		AthleteStatusResponse response = new AthleteStatusResponse(bodyStats(athleteId, checkDate));
		return ResponseEntity.ok(response);
	}

	private BodyStatsDTO[] bodyStats(Long athleteId, ZonedDateTime checkDate) {
		BodyStatsService service = applicationContext.getBean(BodyStatsService.class);
		return service.findNewsByUser(athleteId, checkDate).stream().map(BodyStatsDTO::new)
				.toArray(BodyStatsDTO[]::new);
	}

	@Autowired
	protected void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
