package com.browndwarf.noticeboard.controller.ControllerException;

public class AuthorizeFailException extends RuntimeException {

	private static final long serialVersionUID = 1294051735694038304L;

	public AuthorizeFailException(String msg) {
		super(msg);
	}
}
