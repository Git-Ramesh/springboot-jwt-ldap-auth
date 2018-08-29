package com.rs.ldap.config;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

import com.rs.ldap.model.TokenUser;

//@Component("contextMapper")
public class LdapUserDetailsContextMapper implements UserDetailsContextMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserDetailsContextMapper.class);

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		LOGGER.debug("mapUserFromContext..");
		LOGGER.debug("Username {} " + username);
		TokenUser user = new TokenUser();
		user.setUsername(username);
		user.setFirstname(ctx.getStringAttribute("givenName"));
		user.setLastname(ctx.getStringAttribute("sn"));
		user.setEmail(ctx.getStringAttribute("mail"));
		user.setAuthorities(authorities);
		user.setPassword(ctx.getStringAttribute("userPassword"));
		return user;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		LOGGER.debug("mapUserToContext..");
		LOGGER.debug("UserDetails.class {} " + user.getClass());
		TokenUser tokenUser = (TokenUser) user;
		ctx.setAttributeValue("givenName", tokenUser.getFirstname());
		ctx.setAttributeValue("sn", tokenUser.getLastname());
		ctx.setAttributeValue("mail", tokenUser.getEmail());
		ctx.setAttributeValue("userPassword", tokenUser.getPassword());
		ctx.setAttributeValue("uid", tokenUser.getUsername());
	}
}