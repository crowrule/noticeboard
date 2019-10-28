package com.browndwarf.noticeboard.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.browndwarf.noticeboard.controller.ControllerException.AuthFailException;
import com.browndwarf.noticeboard.controller.ControllerException.AuthorizeFailException;

@Controller
public class CustomErrorContoller implements ErrorController {

    private static final String ERROR_PATH = "/error";
    
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, Model model) throws AuthFailException, AuthorizeFailException, Exception {
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        
    	switch(Integer.valueOf(status.toString())) {
    	case 401 : throw new AuthFailException("Invalid User");
    	case 403 : throw new AuthorizeFailException("Unauthorized User");
    	default :
    		throw new Exception(httpStatus.getReasonPhrase());
    	}

    }

}
