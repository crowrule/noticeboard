package com.browndwarf.noticeboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.browndwarf.noticeboard.controller.ControllerException.AuthFailException;
import com.browndwarf.noticeboard.controller.ControllerException.AuthorizeFailException;
import com.browndwarf.noticeboard.controller.ControllerException.TargetNotFoundException;
import com.browndwarf.noticeboard.vo.CommonResponseVo;

@RestControllerAdvice
public class CommonControllerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public	ResponseEntity<CommonResponseVo> responseException(HttpServletRequest request, Exception e) {

		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Internal Server Error")
						.build()
				);
				
	}
	
	@ExceptionHandler(AuthFailException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public	ResponseEntity<CommonResponseVo> responseUnAuthException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.UNAUTHORIZED.value())
						.message(e.getMessage())
						.build()
				);
				
	}
	
	@ExceptionHandler(TargetNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public	ResponseEntity<CommonResponseVo> responseNotFoundTargetException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.NOT_FOUND.value())
						.message(e.getMessage())
						.build()
				);
				
	}	
	
	@ExceptionHandler(AuthorizeFailException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public	ResponseEntity<CommonResponseVo> responseNoAuthorizationException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.FORBIDDEN.value())
						.message(e.getMessage())
						.build()
				);
				
	}	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public	ResponseEntity<CommonResponseVo> responseParameterSyntaxErrorException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.METHOD_NOT_ALLOWED.value())
						.message("It seems to be weird input value.")
						.build()
				);
				
	}
}
