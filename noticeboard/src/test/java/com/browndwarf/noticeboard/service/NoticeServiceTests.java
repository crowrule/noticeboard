package com.browndwarf.noticeboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.browndwarf.noticeboard.controller.ControllerException.AuthFailException;
import com.browndwarf.noticeboard.controller.ControllerException.AuthorizeFailException;
import com.browndwarf.noticeboard.dto.NoticeDto;
import com.browndwarf.noticeboard.entity.NoticeEntity;
import com.browndwarf.noticeboard.entity.UserEntity;
import com.browndwarf.noticeboard.repository.NoticeRepository;
import com.browndwarf.noticeboard.repository.UserRepository;import com.browndwarf.noticeboard.vo.CommonResponseVo;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NoticeService.class, NoticeRepository.class, UserRepository.class, RoleService.class}, initializers = ConfigFileApplicationContextInitializer.class)
class NoticeServiceTests {

	@Autowired
	NoticeService	noticeService;

	@MockBean
	NoticeRepository	noticeRepository;
	
	@MockBean
	UserRepository	userRepository;
	
	@MockBean
	RoleService	roleService;
	
	@Test
	void testRegisterNotice_withUnauthntifiedUser() {
		// Given
		String	userName = "abc";
		NoticeDto	noticeDto = NoticeDto.builder().title("new").contents("New Conents").userName(userName).build();
		
		//When
		Mockito.when(userRepository.findById(userName)).thenReturn(Optional.empty());
		
		// Then
		assertThrows(AuthFailException.class, () -> {
			noticeService.registerNotice(noticeDto, userName);
	    });
	}
	
	@Test
	void testRegisterNotice_withUnAuthorizedUser() {

		// Given
		String	userName = "abc";
		NoticeDto	noticeDto = NoticeDto.builder().title("new").contents("New Conents").userName(userName).build();
		
		//When
		Mockito.when(userRepository.findById(userName)).thenReturn(Optional.of(UserEntity.builder().userName(userName).role("TESTER").build()));
		Mockito.when(roleService.getWriteRole()).thenReturn(new ArrayList<String>(Arrays.asList("USER", "ADMIN")));
		
		// Then
		assertThrows(AuthorizeFailException.class, () -> {
			noticeService.registerNotice(noticeDto, userName);
	    });
	}

	@Test
	void testRegisterNotice_success() {
		
		// Given
		String	userName = "abc";
		NoticeDto	noticeDto = NoticeDto.builder().title("new").contents("New Conents").userName(userName).build();
		
		//When
		Mockito.when(userRepository.findById(userName)).thenReturn(Optional.of(UserEntity.builder().userName(userName).role("USER").build()));
		Mockito.when(roleService.getWriteRole()).thenReturn(new ArrayList<String>(Arrays.asList("USER", "ADMIN")));
		Mockito.when(noticeRepository.saveAndFlush(any(NoticeEntity.class))).thenReturn(NoticeEntity.builder().id(1).build());
		
		// Then
		Assertions.assertEquals(1, noticeService.registerNotice(noticeDto, userName).getReturnCode());
	}
	
}
