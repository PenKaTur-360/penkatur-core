package es.taixmiguel.penkatur.core.api.profiles.athlete;

import java.time.Instant;
import java.util.List;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Athlete")
@RequestMapping("/api/athlete")
public class AthleteController {

	private BodyStatsService bodyStatsService;

	@PostMapping("/{id}/bodyStats/add")
	public ResponseEntity<TimestampObjectDTO> addBodyStats(@PathVariable("id") Long athleteId,
			@Valid @RequestBody BodyStatsDTO dto) {
		BodyStats bodyStats = bodyStatsService.createBodyStats(athleteId, dto);
		return ResponseEntity.ok(new TimestampObjectDTO(bodyStats));
	}

	@GetMapping("/{id}/bodyStats")
	public ResponseEntity<BodyStatsDTO[]> showBodyStats(@PathVariable("id") Long athleteId,
			@RequestParam Optional<Instant> startDate, @RequestParam Optional<Instant> endDate) {
		BodyStatsDTO[] bodyStats = bodyStatsService
				.findByUser(athleteId, startDate.orElse(Instant.MIN), endDate.orElse(Instant.MAX)).stream()
				.map(BodyStatsDTO::new).toArray(BodyStatsDTO[]::new);
		return ResponseEntity.ok(bodyStats);
	}

	@GetMapping("/{id}/status")
	public ResponseEntity<AthleteStatusResponse> showStatus(@PathVariable("id") Long athleteId,
			@RequestParam Instant checkDate) {
		List<BodyStats> bodyStats = bodyStatsService.findNewsByUser(athleteId, checkDate);
		AthleteStatusResponse response = new AthleteStatusResponse(bodyStats);
		return ResponseEntity.ok(response);
	}

	@Autowired
	protected void setTokenService(BodyStatsService service) {
		this.bodyStatsService = service;
	}
}
