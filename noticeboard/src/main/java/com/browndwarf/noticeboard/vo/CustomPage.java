package com.browndwarf.noticeboard.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomPage {

	private int	page;
	
	private int	size;
	
	private List<String>	sort;
}
