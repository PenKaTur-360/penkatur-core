package es.taixmiguel.penkatur.core.profiles.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserTokenType;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.model.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

	Optional<UserToken> findByTypeAndToken(UserTokenType type, String token);

	@Modifying
	int deleteByUser(User user);
}
