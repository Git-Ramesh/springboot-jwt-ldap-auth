package com.rs.ldap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.InvalidCriteriaException;
import com.rs.ldap.model.Group;
import com.rs.ldap.model.TokenUser;
import com.rs.ldap.repository.GroupRepo;
import com.rs.ldap.repository.LdapRepo;
import com.rs.ldap.util.JwtTokenUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private LdapRepo ldapRepo;
	@Autowired
	private GroupRepo groupRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public String welcome() {
		return "Welcome to LDAP Authentication and Authorization";
	}
	
	@PostMapping
	public String getAuthUserToken(@RequestBody String loginInfo) {
		try {
			JSONObject login = new JSONObject(loginInfo);
			
		} catch (JSONException jsone) {
			LOGGER.error(jsone.getMessage(), jsone);
		}
		return null;
	}
	@PostMapping("/login")
	public boolean login(@RequestBody String loginInfo) {
		boolean isAuth = false;
		try {
			JSONObject login = new JSONObject(loginInfo);
			isAuth = ldapRepo.authenticate(login.getString("username"), login.getString("password"));
			System.out.println(ldapRepo.getUser(login.getString("username")));
		} catch (JSONException jsone) {
			LOGGER.error(jsone.getMessage(), jsone);
		}
		return isAuth;
	}
	  @PostMapping("/authenticate")
	    public String token(@RequestBody String loginInfo) throws IOException {
	        String token  = null;
	        String result = loginInfo;
	        System.out.println(result);
	        JSONObject jsonUsernameAndPassword = new JSONObject(result);
	        JSONObject resp = new JSONObject();
	       
	        String username = jsonUsernameAndPassword.getString("username");
	        String password = jsonUsernameAndPassword.getString("password");
	        List<GrantedAuthority> authorities = new ArrayList<>();
	       if( ldapRepo.authenticate(username, password) ) {
	    	   TokenUser tokenUser = ldapRepo.getUser(username);
	    	  List<Group> groups = (List<Group>) groupRepo.findAll();
	    	  for(Group g : groups) {
	    		  if(g.getUniqueMember().contains(ldapRepo.getUserDn(tokenUser.getUsername()))){
	    			  System.out.println(g.getGroupname());
	    			  authorities.add(new SimpleGrantedAuthority("ROLE_"+g.getGroupname().toUpperCase()));
	    		  }
	    	  }
	    	  System.out.println(authorities);
	    	  tokenUser.setAuthorities(authorities);
		        if(tokenUser != null) {
		            Authentication auth = new UsernamePasswordAuthenticationToken(tokenUser, tokenUser.getPassword(), tokenUser.getAuthorities());
		            SecurityContextHolder.getContext().setAuthentication(auth);
		            if(auth.isAuthenticated()) {
		               token = jwtTokenUtil.createToken(tokenUser.getUsername(), tokenUser.getAuthorities()
		            		   .stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()));
		            }
		        }
		        resp.put("username" , username);
		        resp.put("authorities", tokenUser.getAuthorities());
		        resp.put("token", token);

		        return resp.toString();
	       }
	       else {
	    	   throw new InvalidCriteriaException("Username or password invalid");
	       }
	    }
	  
	  @GetMapping("/group/all")
	  public List<Group> all() {
		  return (List<Group>) groupRepo.findAll();
	  }
}
