package com.opentext.bn.solutiondesigner.configuration;

import org.gitlab4j.api.GitLabApiException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.gxs.orch.rest.client.gitlab.OrchGitlabRestClient;

@Configuration
public class SolutionDesignerConfiguration {

	@Bean
	public DareEnvironmentProperties dareEnvironmentProperties() {
		return DareEnvironmentProperties.getInstance();
	}

	@Bean
	@RequestScope
	public OrchGitlabRestClient orchGitlabRestClient() throws GitLabApiException {
		return new OrchGitlabRestClient();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
}
