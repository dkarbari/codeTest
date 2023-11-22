package com.opentext.bn.solutiondesigner.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.opentext.bn.solutiondesigner")).build()
				.useDefaultResponseMessages(false).apiInfo(apiInfo());

	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Solution Designer Internal API", "Used to integrate SD with various components", "", "",
				null, "", "", Collections.emptyList());
	}
}
