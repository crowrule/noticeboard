package com.browndwarf.noticeboard.controller;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.browndwarf.noticeboard.controller.ControllerException.AuthFailException;
import com.browndwarf.noticeboard.entity.UserEntity;
import com.browndwarf.noticeboard.repository.UserRepository;
import com.browndwarf.noticeboard.security.JwtTokenManager;

import io.swagger.annotations.Api;

@Api(tags= {"User"})
@RestController
@RequestMapping(value= "/")
public class UserController {

	@Autowired
	UserRepository	userRepository;

	@Autowired
	private JwtTokenManager	jwtTokenManager;
	
	@GetMapping(value = "/login")
	public ResponseEntity<String> login(@RequestParam String id, @RequestParam String password) throws Exception {
	 
		UserEntity user = userRepository.findById(id).orElseThrow(bind(AuthFailException::new, "No user Information."));
	        
		if (!passwordEncoder().matches(password, user.getPassword())) 
			throw new AuthFailException("Invalid Password");
	 
		return ResponseEntity.ok()
//				.header("Access-Control-Allow-Origin", "*")
				.body(jwtTokenManager.createToken(user.getUsername(), user.getRole()));
	}
	
	private <T, R> Supplier<R> bind(Function<T,R> fn, T val) {
		return () -> fn.apply(val);
	}

	private PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
