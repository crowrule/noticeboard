package com.browndwarf.noticeboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMethod;

import com.browndwarf.noticeboard.vo.CustomPage;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private final TypeResolver resolver = new TypeResolver();
	
	@Bean
	public Docket defaultSwaggerConfig() {
		return	new Docket(DocumentationType.SWAGGER_2)
				.enable(true)
				.groupName("basic")
				.select()
					.paths(PathSelectors.any())
					.paths(Predicates.not(PathSelectors.regex("/error.*")))
					.build()
				.apiInfo(getApiInfo())
				.useDefaultResponseMessages(false)
				.alternateTypeRules(
						AlternateTypeRules.newRule(resolver.resolve(Pageable.class), resolver.resolve(CustomPage.class))
					)
				.globalResponseMessage(RequestMethod.GET, 
					Lists.newArrayList(
						new ResponseMessageBuilder().code(204).message("Operation Successed. But there is not element").build(),
						new ResponseMessageBuilder().code(500).message("Fail to execute request because of inernal error").build()
					)
				)
				.globalResponseMessage(RequestMethod.POST, 
					Lists.newArrayList(
						new ResponseMessageBuilder().code(401).message("Unauthenticated user.").build(),
						new ResponseMessageBuilder().code(403).message("Forbidden Request. Need to authorization.").build(),
						new ResponseMessageBuilder().code(405).message("Request body is wrong").build(),
						new ResponseMessageBuilder().code(500).message("Fail to execute request because of inernal error").build()
					)
				)
				.globalResponseMessage(RequestMethod.PUT, 
					Lists.newArrayList(
						new ResponseMessageBuilder().code(401).message("Unauthenticated user.").build(),
						new ResponseMessageBuilder().code(403).message("Forbidden Request. Need to authorization.").build(),
						new ResponseMessageBuilder().code(405).message("Request body is wrong").build(),
						new ResponseMessageBuilder().code(500).message("Fail to execute request because of inernal error").build()
					)
				)
				.globalResponseMessage(RequestMethod.DELETE, 
					Lists.newArrayList(
						new ResponseMessageBuilder().code(401).message("Unauthenticated user.").build(),
						new ResponseMessageBuilder().code(403).message("Forbidden Request. Need to authorization.").build(),
						new ResponseMessageBuilder().code(405).message("Request body is wrong").build(),
						new ResponseMessageBuilder().code(500).message("Fail to execute request because of inernal error").build()
					)
				);
				
	}
	
	private ApiInfo	getApiInfo() {
		
		return new ApiInfoBuilder()
				.title("Notice API Documentation")
				.description("This document is build for test")
				.build();
	}
}
