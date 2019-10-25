package com.browndwarf.noticeboard.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.browndwarf.noticeboard.entity.NoticeEntity;
import com.browndwarf.noticeboard.entity.UserEntity;
import com.browndwarf.noticeboard.repository.NoticeRepository;
import com.browndwarf.noticeboard.repository.UserRepository;
import com.browndwarf.noticeboard.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationDataInitializer implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	NoticeRepository	noticeRepository;
	
	@Autowired
	UserRepository	userRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("Insert Test Notice Data");
		
		List<UserEntity> sampleUserList = new ArrayList<UserEntity>(Arrays.asList(
				UserEntity.builder().userName("admin").password(passwordEncoder().encode("admin")).role("ADMIN").build(),
				UserEntity.builder().userName("user").password(passwordEncoder().encode("password")).role("USER").build()
			));
		userRepository.saveAll(sampleUserList);
		
		List<NoticeEntity> sampleNoticeList = new ArrayList<NoticeEntity>(Arrays.asList(
				NoticeEntity.builder().title("Example 1").contents("The First Example Content").user(sampleUserList.get(0)).build(),
				NoticeEntity.builder().title("Example 2").contents("The Second Example Content").user(sampleUserList.get(0)).build()
			));
		
		noticeRepository.saveAll(sampleNoticeList);
		
	}

	
	private PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
