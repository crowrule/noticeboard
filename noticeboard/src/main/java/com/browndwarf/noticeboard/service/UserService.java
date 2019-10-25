package com.browndwarf.noticeboard.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.browndwarf.noticeboard.entity.UserEntity;
import com.browndwarf.noticeboard.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository	userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		return userRepository.findById(userName).orElse(null);
	}

	public	String	getUserRole(String userName) {
		
		Optional<UserEntity> opUser = userRepository.findById(userName);
		
		return (opUser.isPresent() ? opUser.get().getRole() : "");
	}
}
