package com.browndwarf.noticeboard.controller.ControllerException;

public class AuthFailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3090504111680272225L;
	
	public AuthFailException(String msg) {
		super(msg);
	}

}
