package es.taixmiguel.penkatur.core.profiles.user.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.taixmiguel.penkatur.core.profiles.user.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserSecretsRepository secretsRepository;
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserSecrets secrets = userRepository.findByEmail(username).map(secretsRepository::findByUser)
				.orElseGet(Optional::empty).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new UserDetailsImpl(secrets.getUser(), secrets.getPassword(), secrets.isPasswordExpirated());
	}

	@Autowired
	public void setSecretsRepository(UserSecretsRepository secretsRepository) {
		this.secretsRepository = secretsRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
