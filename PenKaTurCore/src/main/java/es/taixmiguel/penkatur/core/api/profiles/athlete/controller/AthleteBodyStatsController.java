package es.taixmiguel.penkatur.core.api.profiles.athlete.controller;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.taixmiguel.penkatur.core.api.TimestampObjectDTO;
import es.taixmiguel.penkatur.core.api.profiles.athlete.dto.BodyStatsDTO;
import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import es.taixmiguel.penkatur.core.profiles.athlete.service.BodyStatsService;
import es.taixmiguel.penkatur.core.tools.DateTimeUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Athlete")
@RequestMapping("/api/athlete/{id}/bodyStats")
public class AthleteBodyStatsController {

	private BodyStatsService bodyStatsService;

	@PostMapping("/add")
	public ResponseEntity<TimestampObjectDTO> addBodyStats(@PathVariable("id") Long athleteId,
			@Valid @RequestBody BodyStatsDTO dto) {
		BodyStats bodyStats = bodyStatsService.createBodyStats(athleteId, dto);
		return ResponseEntity.ok(new TimestampObjectDTO(bodyStats));
	}

	@GetMapping("")
	public ResponseEntity<BodyStatsDTO[]> showBodyStats(@PathVariable("id") Long athleteId,
			@RequestParam Optional<ZonedDateTime> startDate, @RequestParam Optional<ZonedDateTime> endDate) {
		BodyStatsDTO[] bodyStats = bodyStatsService
				.findByUser(athleteId, startDate.orElse(DateTimeUtils.getMinimumZonedDateTime()),
						endDate.orElse(DateTimeUtils.getMaximumZonedDateTime()))
				.stream().map(BodyStatsDTO::new).toArray(BodyStatsDTO[]::new);
		return ResponseEntity.ok(bodyStats);
	}

	@Autowired
	protected void setBodyStatsService(BodyStatsService service) {
		this.bodyStatsService = service;
	}
}
