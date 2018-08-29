package com.rs.ldap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "TOKEN_USER")
public class TokenUser implements UserDetails, Serializable {
	private static final long serialVersionUID = -5302977325901685016L;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	
	/*@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}*/
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
