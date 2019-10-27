package com.browndwarf.noticeboard.vo;

import com.browndwarf.noticeboard.dto.NoticeDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeVo extends NoticeDto {

	private long id;
	
	private String createTime;	
	
	private String lastUpdateime;

	public NoticeVo() {
		super();
		
	}
}
