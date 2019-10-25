package com.browndwarf.noticeboard.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity(name="user")
@Getter @Setter
public class UserEntity  implements UserDetails {
	
	private static final long serialVersionUID = -1L; 
	
	@Id
	@Column(name="id")
	String	username;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
    private String password;
	
	private String role;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private	Timestamp	createdTime;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private	Timestamp	updateTime;

	@Override 
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Arrays.asList(new SimpleGrantedAuthority(this.role));
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	

	
	@Builder
	public	UserEntity(String userName, String password, String role) {
		this.username = userName;
		this.password = password;
		this.role = role;
	}

}
