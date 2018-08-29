package com.rs.ldap.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rs.ldap.filter.JWTAuthFilter;
import com.rs.ldap.model.TokenUser;
import com.rs.ldap.repository.GroupRepoAdditional;
import com.rs.ldap.repository.GroupRepoImpl;
import com.rs.ldap.util.JwtTokenUtil;

@Configuration
@EnableWebSecurity
@EnableLdapRepositories
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private Environment env;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/", "/resources/**", "/static/**", "/public/**", "/h2-console/**", " /*.html",
				"/**/*.html", "/**/*.js", "/**/*.gif", "/**/*.ico", "/swagger-ui/**", "/swagger-resources/**",
				"/api-docs/**", "/user/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.anyRequest().authenticated().and().addFilterBefore(new JWTAuthFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	}

	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://localhost:10389");
		contextSource.setBase("dc=radiant,dc=com");
		return contextSource;
	}
	
	@Bean 
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}
	@Bean
	public GroupRepoAdditional groupRepoAdditional() {
		return new GroupRepoImpl();
	}
	@Bean
    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            @Override
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                TokenUser user = (TokenUser) super.mapUserFromContext(ctx, username, authorities);
               return user;
            }
        };
    }
}