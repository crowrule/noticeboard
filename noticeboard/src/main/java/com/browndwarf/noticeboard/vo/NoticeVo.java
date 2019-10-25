package com.browndwarf.noticeboard.vo;

import com.browndwarf.noticeboard.dto.NoticeDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeVo extends NoticeDto {

	public long id;
	
	public String createTime;	
	
	public String lastUpdateime;
	
}
