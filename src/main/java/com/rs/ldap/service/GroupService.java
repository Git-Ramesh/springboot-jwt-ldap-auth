package com.rs.ldap.service;

import java.util.List;

import com.rs.ldap.model.Group;

public interface GroupService {
	List<Group> findAll();
}
