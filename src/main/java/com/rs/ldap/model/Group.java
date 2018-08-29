package com.rs.ldap.model;

import java.util.Set;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entry(objectClasses = {"groupOfUniqueNames","top"})
@Data
public class Group {
	@Id
	@JsonIgnore
	private Name dn;
	@Attribute(name = "uniqueMember")
	private Set<String> uniqueMember;
	@Attribute(name = "cn")
	private String groupname;
}
