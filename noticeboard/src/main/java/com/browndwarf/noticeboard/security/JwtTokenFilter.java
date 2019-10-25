package com.browndwarf.noticeboard.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.browndwarf.noticeboard.vo.CommonResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private JwtTokenManager jwtTokenManager;

	public JwtTokenFilter(JwtTokenManager jwtTokenManager) {
		this.jwtTokenManager = jwtTokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		
		String token = jwtTokenManager.getTokenFromHeader(httpServletRequest);

		try {
			if (token != null && jwtTokenManager.validateToken(token)) {
				Authentication auth = jwtTokenManager.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}catch (Exception ex) {
			//this is very important, since it guarantees the user is not authenticated at all
			log.error(">>> Wrong Token!!!");
			SecurityContextHolder.clearContext();
			

			Map<String, Object> messageMap = new HashMap<String, Object>();
			messageMap.put("returnCode", HttpStatus.FORBIDDEN.value());
			messageMap.put("message", "You are not authentified");
			
			ObjectMapper mapper = new ObjectMapper();

			httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
			httpServletResponse.setContentType("application/json");
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.getWriter().write(mapper.writeValueAsString(
					CommonResponseVo.builder()
						.returnCode(HttpStatus.FORBIDDEN.value())
						.message("You are not authentified")
						.build()
				)
			);

			return;
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}
