package com.browndwarf.noticeboard.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private JwtTokenManager	jwtTokenManager;
	
	
	public JwtTokenFilterConfigurer(JwtTokenManager jwtTokenManager) {
		this.jwtTokenManager = jwtTokenManager;
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
	    JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenManager);
	    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}	
}
