package com.browndwarf.noticeboard.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.browndwarf.noticeboard.dto.NoticeDto;
import com.browndwarf.noticeboard.security.JwtTokenManager;
import com.browndwarf.noticeboard.service.NoticeService;
import com.browndwarf.noticeboard.service.RoleService;
import com.browndwarf.noticeboard.service.UserService;
import com.browndwarf.noticeboard.vo.CommonResponseVo;
import com.browndwarf.noticeboard.vo.NoticeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags= {"Notice"})
@RestController
@RequestMapping(value= {"/api"}, produces="application/json")
public class NoticeController {

	@Autowired
	NoticeService	noticeService;
	
	@Autowired
	JwtTokenManager	jwtTokenManager;
	
	@Autowired
	UserService	userService;
	
	@Autowired 
	RoleService roleService;

	
	@ApiOperation(tags= {"Notice"}, value="Register new Notice")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="Successfully registered.")
	})
	@PostMapping("/notice")
	@CrossOrigin
	public ResponseEntity<CommonResponseVo> registerNotice(
			@RequestHeader(value="X-AUTH-KEY", required=false) String  token,
			@RequestBody NoticeDto reqParam){

		String userName = reqParam.getUserName();
		log.info("Enter Notice Regestration process : User({})", userName);
		
		CommonResponseVo response = noticeService.registerNotice(reqParam, userName);

		log.info("Complete Notice Regestration process");

		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(tags= {"Notice"}, value="Update the previous notice")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="Successfully updated."),
		@ApiResponse(code=404, message="Not Found Target Notice")
	})
	@PutMapping("/notice/{id}")
	@CrossOrigin
	public ResponseEntity<CommonResponseVo> updateNotice(
			@RequestHeader(value="X-AUTH-KEY", required=false) String  token,
			@PathVariable("id") String id,
			@RequestBody NoticeDto reqParam){
		
		String userName = reqParam.getUserName();
		log.info("Enter Notice Update process : User({})", userName);
		
		CommonResponseVo response = noticeService.updateNotice(Long.parseLong(id), reqParam, userName);

		log.info("Complete Notice Update process");
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(tags= {"Notice"}, value="Delete a notice")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="Successfully deleted."),
		@ApiResponse(code=404, message="Not Found Target Notice")
	})
	@DeleteMapping("/notice/{id}")
	@CrossOrigin
	public ResponseEntity<CommonResponseVo> deleteNotice(
			@RequestHeader(value="X-AUTH-KEY", required=false) String  token,
			@PathVariable("id") String id,
			@RequestParam String userName){

		log.info("Enter Notice Delete process with id({}) : User({})", id, userName);
		
		long entityId = Long.parseLong(id);
		CommonResponseVo response = noticeService.deleteNotice(entityId, userName);

		log.info("Complete Notice Delete process");
		
		return ResponseEntity.ok().body(response);
	}
	
	@ApiOperation(tags= {"Notice"}, value="Retrieve a notice")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="Successfully retrievd.")
	})
	@GetMapping("/notice/{id}")
	@CrossOrigin
	public ResponseEntity<NoticeVo> retrieveNotice(
			@PathVariable("id") String id){
		log.info("Enter Notice retrieve process with id({})", id);
		
		long entityId = Long.parseLong(id);
		Optional<NoticeVo> retrievedResult = noticeService.getNotice(entityId);

		log.info("Complete Notice retrieve process");
		
		if (retrievedResult.isPresent()) return ResponseEntity.ok().body(retrievedResult.get());
		else return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(tags= {"Notice"}, value="Retrieve notices with page information")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="Successfully retrievd.")
	})
	@GetMapping("/notice")
	@CrossOrigin(value = {"*"}, exposedHeaders = {"X-TOTAL-COUNT","X-TOTAL-PAGE", "X-PAGE-SIZE", "X-CURRENT-PAGE-SIZE", "X-CURRENT-PAGE"})
	public ResponseEntity<List<NoticeVo>> retrieveAllNotice(
			@PageableDefault(page=0, size=5, sort="id", direction=Direction.DESC) Pageable pageable
	){
		log.info("Enter Notice Retrievd process");
		
		Page<NoticeVo> retrievedResult = noticeService.getNoticesWithPage(pageable);
		HttpHeaders	header = new HttpHeaders();
		header.set("X-PAGE-SIZE", String.valueOf(retrievedResult.getSize()));
		header.set("X-TOTAL-COUNT", String.valueOf(retrievedResult.getTotalElements()));
		header.set("X-TOTAL-PAGE", String.valueOf(retrievedResult.getTotalPages()));
		header.set("X-CURRENT-PAGE-SIZE", String.valueOf(retrievedResult.getNumberOfElements()));
		header.set("X-CURRENT-PAGE", String.valueOf(retrievedResult.getNumber()));
		
		if (retrievedResult.getNumberOfElements() == 0) return ResponseEntity.noContent().headers(header).build();		
		
		log.info("Complete Notice Retrievd process");
		
		return ResponseEntity.ok().headers(header).body(retrievedResult.getContent());
	}
}
