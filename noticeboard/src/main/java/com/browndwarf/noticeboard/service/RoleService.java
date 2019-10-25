package com.browndwarf.noticeboard.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

	private final static List<String> roleList = new ArrayList<String>(Arrays.asList("ADMIN", "USER"));

	public List<String> getWriteRole(){
		return roleList;
	}
	
	public List<String> getForcedDeleteRole() {
		return  roleList.subList(0, 1);
	}
}
