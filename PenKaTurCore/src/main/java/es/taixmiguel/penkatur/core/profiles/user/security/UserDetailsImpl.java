package es.taixmiguel.penkatur.core.profiles.user.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.taixmiguel.penkatur.core.profiles.user.attributes.UserStatus;
import es.taixmiguel.penkatur.core.profiles.user.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Collection<? extends GrantedAuthority> authorities;
	private final boolean passwordExpirated;
	private final String password;
	private final User user;

	public UserDetailsImpl(User user, String password, boolean passwordExpirated) {
		this.user = user;
		this.password = password;
		this.authorities = new ArrayList<>();
		this.passwordExpirated = passwordExpirated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserStatus.EXPIRED != user.getStatus();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserStatus.LOCKED != user.getStatus();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !passwordExpirated;
	}

	@Override
	public boolean isEnabled() {
		return UserStatus.ACTIVE == user.getStatus();
	}
}
