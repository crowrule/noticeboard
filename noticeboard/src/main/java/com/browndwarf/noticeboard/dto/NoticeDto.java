package com.browndwarf.noticeboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeDto {

	private String	title;
	
	private String	contents;
	
	private String userName;
	
	@Builder
	public	NoticeDto(String title, String contents, String	userName) {
		this.title = title;
		this.contents = contents;
		this.userName = userName;
	}

	public NoticeDto() {

	}
	
}
