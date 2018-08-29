package com.rs.ldap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rs.ldap.model.Group;
import com.rs.ldap.repository.GroupRepo;
import com.rs.ldap.service.GroupService;

public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupRepo groupRepo;

	@Override
	public List<Group> findAll() {
		return (List<Group>) groupRepo.findAll();
	}
	
}
