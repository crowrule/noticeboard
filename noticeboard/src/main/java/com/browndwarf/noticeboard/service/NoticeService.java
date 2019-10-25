package com.browndwarf.noticeboard.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.browndwarf.noticeboard.controller.ControllerException.AuthFailException;
import com.browndwarf.noticeboard.controller.ControllerException.AuthorizeFailException;
import com.browndwarf.noticeboard.controller.ControllerException.TargetNotFoundException;
import com.browndwarf.noticeboard.dto.NoticeDto;
import com.browndwarf.noticeboard.entity.NoticeEntity;
import com.browndwarf.noticeboard.entity.UserEntity;
import com.browndwarf.noticeboard.repository.NoticeRepository;
import com.browndwarf.noticeboard.repository.UserRepository;
import com.browndwarf.noticeboard.vo.CommonResponseVo;
import com.browndwarf.noticeboard.vo.NoticeVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {

	@Autowired 
	NoticeRepository	noticeRepository;
	
	@Autowired
	UserRepository	userRepository;
	
	@Autowired
	RoleService	roleService;
	
	public CommonResponseVo registerNotice(NoticeDto noticeDto, String username) {
		
		Optional<UserEntity>	opUser = userRepository.findById(username);

		if (!opUser.isPresent()) throw new AuthFailException("No User Information.");
		else if (!roleService.getWriteRole().contains(opUser.get().getRole())) throw new AuthorizeFailException("You don't have permission to write.");
		
		NoticeEntity	noticeEntity = NoticeEntity.builder()
										.title(noticeDto.getTitle())
										.contents(noticeDto.getContents())
										.user(opUser.get())
										.build();
		
		noticeEntity = noticeRepository.saveAndFlush(noticeEntity);
		log.info(">>> Saved new Notice : id({})", noticeEntity.getId());
		
		return CommonResponseVo.builder()
				.returnCode(noticeEntity.getId())
				.message("New notice is registered.")
				.build();
	}
	
	public CommonResponseVo updateNotice(long id, NoticeDto noticeDto, String username) {
		
		Optional<NoticeEntity> opNotice = noticeRepository.findById(id);
		Optional<UserEntity>	opUser = userRepository.findById(username);
		
		if (!opNotice.isPresent()) throw new TargetNotFoundException(new StringBuilder("There is not notice with id(").append(id).append(")").toString());
		else if (!opUser.isPresent()) throw new AuthFailException("No User Information.");
		else if (!opNotice.get().getUser().getUsername().equals(username)) throw new AuthorizeFailException("Only writer can be modified.");
		
		NoticeEntity noticeEntity = opNotice.get();
		
		noticeEntity.setTitle(noticeDto.getTitle());
		noticeEntity.setContents(noticeDto.getContents());

		noticeEntity = noticeRepository.saveAndFlush(noticeEntity);
		log.info(">>> Update the previous Notice : id({})", noticeEntity.getId());
		
		return CommonResponseVo.builder()
				.returnCode(id)
				.message("This notice is updated.")
				.build();
	
	}
	
	@Transactional
	public CommonResponseVo deleteNotice(long id, String username) {
		
		Optional<NoticeEntity> opNotice = noticeRepository.findById(id);
		Optional<UserEntity>	opUser = userRepository.findById(username);
		
		if (!opNotice.isPresent()) throw new TargetNotFoundException(new StringBuilder("There is not notice with id(").append(id).append(")").toString());
		else if (!opUser.isPresent()) throw new AuthFailException("No User Information.");
		else if (!opNotice.get().getUser().getUsername().equals(username) && 
				!roleService.getForcedDeleteRole().contains(opUser.get().getRole())) 
			throw new AuthorizeFailException("You don't have permission to delete.");

		
		noticeRepository.deleteById(id);
	
		return CommonResponseVo.builder()
				.returnCode(id)
				.message("This notice is deleted.")
				.build();
	}
	
	public Optional<NoticeVo> getNotice(long id) {
		
		Optional<NoticeEntity>	noticeEntity =  noticeRepository.findById(id);
		
		return Optional.of(convertToNoticeVO(noticeEntity.orElse(null)));
	}
	
	public List<NoticeVo> getAllNotice() {
		
		List<NoticeVo> allNotice = noticeRepository.findAll().stream().map(entity->convertToNoticeVO(entity)).collect(Collectors.toList());
		
		return allNotice;
	}
	
	public Page<NoticeVo> getNoticesWithPage(Pageable page) {
		
		Page<NoticeVo> pagedNotice = noticeRepository.findAll(page).map(this::convertToNoticeVO);;

		
		return pagedNotice;
	}
	
	private NoticeVo convertToNoticeVO(NoticeEntity entity) {
		if (entity == null) return null;
		
		NoticeVo	vo = new NoticeVo();
		
		vo.setId(entity.getId());
		vo.setTitle(entity.getTitle());
		vo.setContents(entity.getContents());
		vo.setUserName(entity.getUser().getUsername());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		vo.setCreateTime(formatter.format(entity.getCreatedTime().toLocalDateTime()));
		vo.setLastUpdateime(formatter.format(entity.getUpdateTime().toLocalDateTime()));
		
		return vo;
	}
}
