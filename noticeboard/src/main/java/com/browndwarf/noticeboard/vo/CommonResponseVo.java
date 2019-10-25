package com.browndwarf.noticeboard.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter	@Setter
public class CommonResponseVo {

	long	returnCode;
	
	String	message;
	
	@Builder
	public	CommonResponseVo(long returnCode, String message) {
		this.returnCode = returnCode;
		this.message = message;
	}
}
