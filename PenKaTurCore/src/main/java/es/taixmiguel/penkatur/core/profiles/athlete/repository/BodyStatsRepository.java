package es.taixmiguel.penkatur.core.profiles.athlete.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.taixmiguel.penkatur.core.profiles.athlete.model.BodyStats;
import es.taixmiguel.penkatur.core.profiles.user.model.User;

@Repository
public interface BodyStatsRepository extends JpaRepository<BodyStats, Long> {

	List<BodyStats> findByUserAndTimestampAfter(User user, Instant instant);

	List<BodyStats> findAllByUserAndRegisterTimeBetween(User user, Instant startTime, Instant endTime);
}
