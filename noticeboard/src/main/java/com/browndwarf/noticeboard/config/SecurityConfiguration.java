package com.browndwarf.noticeboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.browndwarf.noticeboard.security.JwtTokenFilterConfigurer;
import com.browndwarf.noticeboard.security.JwtTokenManager;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenManager	jwtTokenManager;
	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
    			.httpBasic().disable()
    			.csrf().disable()
    			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    			.and()
    				.authorizeRequests()
    					.antMatchers("/login").permitAll()
    					.antMatchers(HttpMethod.GET, "/api/**").permitAll()
    					.anyRequest().authenticated();

    	httpSecurity.apply(new JwtTokenFilterConfigurer(jwtTokenManager));
    }
    
    @Override 
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
        		"/h2-console/**",
        		"/v2/api-docs", 
        		"/swagger-resources/**",
                "/swagger-ui.html", 
                "/webjars/**", 
                "/swagger/**");
 
    }
}
