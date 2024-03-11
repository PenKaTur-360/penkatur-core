package es.taixmiguel.penkatur.core.profiles.user.security.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.taixmiguel.penkatur.core.profiles.user.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = -4292300756772591259L;

	private Long id;
	private String email;
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(User user, String password) {
		this.id = user.getId();
		this.password = password;
		this.email = user.getEmail();
		this.authorities = new ArrayList<>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
