package com.rs.ldap.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import com.rs.ldap.model.Group;

public interface GroupRepo extends LdapRepository<Group>, GroupRepoAdditional {

}
