package com.rs.ldap.repository;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.AuthenticatedLdapEntryContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapEntryIdentification;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;

import com.rs.ldap.model.Group;
import com.rs.ldap.model.TokenUser;

@Component
public class LdapRepo {

	@Autowired
	private LdapTemplate ldapTemplate;

	public boolean authenticate(String username, String password) {
		boolean isAuthenticated = false;
		AuthenticatedLdapEntryContextMapper<DirContextOperations> mapper = new AuthenticatedLdapEntryContextMapper<DirContextOperations>() {

			@Override
			public DirContextOperations mapWithContext(DirContext ctx,
					LdapEntryIdentification ldapEntryIdentification) {
				DirContextOperations dirContextOperations = null;
				try {
					System.out.println("relativeName: " + ldapEntryIdentification.getRelativeName());
					dirContextOperations = (DirContextOperations) ctx.lookup(ldapEntryIdentification.getRelativeName());
				} catch (NamingException ne) {
					ne.printStackTrace();
				}
				return dirContextOperations;
			}
		};
		DirContextOperations dco = ldapTemplate.authenticate(LdapQueryBuilder.query().where("uid").is(username),
				password, mapper);
		isAuthenticated = (dco != null) && (dco.getStringAttribute("uid").equals(username));
		return isAuthenticated;
	}
	public TokenUser getUser(String username) {
		 List<TokenUser> list=ldapTemplate.search(LdapQueryBuilder.query().where("objectClass").is("organizationalPerson").and("uid").is(username), new TokenUserAttributeMapper());
		 System.out.println(list.size());
		 return list.get(0);
	}
	public void test(String userDn) {
		ldapTemplate.search(LdapQueryBuilder.query().where("objectClass").is("groupOfUniqueNames").and("uniqueMember").is(userDn), new GroupMapper());
	}
	private class GroupMapper implements AttributesMapper<Group> {

		@Override
		public Group mapFromAttributes(Attributes attributes) throws NamingException {
			Group group = new Group();
			group.setGroupname(attributes.get("cn").get(0).toString());
			//attributes.get
			return null;
		}
		
	}
	private class TokenUserAttributeMapper implements AttributesMapper<TokenUser> {

		@Override
		public TokenUser mapFromAttributes(Attributes attributes) throws NamingException {
			TokenUser tokenUser = new TokenUser();
			tokenUser.setUsername(attributes.get("uid").get(0).toString());
			tokenUser.setFirstname(attributes.get("givenName").get(0).toString());
			tokenUser.setLastname(attributes.get("sn").get(0).toString());
			tokenUser.setPassword(attributes.get("userPassword").get(0).toString());
			tokenUser.setEmail(attributes.get("mail").get(0).toString());
			return tokenUser;
		}
	}
	public String getUserDn(String username) {
		return "uid="+username+",ou=people,dc=radiant,dc=com";
	}
}
