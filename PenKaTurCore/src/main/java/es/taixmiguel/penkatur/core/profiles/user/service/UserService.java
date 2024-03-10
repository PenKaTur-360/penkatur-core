package es.taixmiguel.penkatur.core.profiles.user.service;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.taixmiguel.penkatur.core.exceptions.DuplicatedUserException;
import es.taixmiguel.penkatur.core.profiles.user.model.User;
import es.taixmiguel.penkatur.core.profiles.user.repository.UserRepository;
import es.taixmiguel.penkatur.core.tools.log.Log;

@Service
public class UserService {

	private UserRepository userRepo;

	public User createUser(User user) throws DuplicatedUserException {
		try {
			user = userRepo.save(user);
			Log.trace(getClass(),
					String.format("A user with the email %s has been created successfully.", user.getEmail()));
			return user;
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
			Log.trace(getClass(), String.format("User with email %s already exists in the system.", user.getEmail()));
			throw new DuplicatedUserException();
		}
	}

	public Optional<User> findUser(Long id) {
		return userRepo.findById(id);
	}

	public Optional<User> findUser(String email) {
		return userRepo.findByEmail(email);
	}

	public User updateUser(User user) {
		Log.trace(getClass(), String.format("The user with ID %d is going to be updated.", user.getId()));
		return userRepo.saveAndFlush(user);
	}

	public void deleteUser(User user) {
		userRepo.delete(user);
		Log.trace(getClass(),
				String.format("The user with ID %d has been successfully deleted from the system.", user.getId()));
	}

	@Autowired
	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
}
