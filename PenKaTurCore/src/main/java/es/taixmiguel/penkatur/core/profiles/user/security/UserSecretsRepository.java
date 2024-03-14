package es.taixmiguel.penkatur.core.profiles.user.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.taixmiguel.penkatur.core.profiles.user.model.User;

@Repository
interface UserSecretsRepository extends JpaRepository<UserSecrets, Long> {

	Optional<UserSecrets> findByUser(User user);
}
