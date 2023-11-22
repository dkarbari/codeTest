package com.opentext.bn.solutiondesigner.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opentext.bn.solutiondesigner.vo.connector.Connector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/a2aconnectors")
public class A2AController extends BaseController  {

	@Value("${solution.designer.a2a.server.url}")
	private String connectorApiEndpoint;

	@Autowired
	private RestTemplate restTemplate;

	final Logger logger = LoggerFactory.getLogger(A2AController.class);

	@ApiOperation(value = "Retrieve connector information from CMD using service code")
	@RequestMapping(value = { "/{serviceCode}" }, method = RequestMethod.GET, produces = "application/json")
	@ApiResponses(value = {})

	public Connector retrieveConnectorInfoByCode(
			@ApiParam(value = "Service code of connector name to be fetched", required = true, example = "a2aSforceService(34)v1.0.0")
			@PathVariable
			final String serviceCode) {

		logger.info("Service code received : {}", serviceCode);

		final String url = UriComponentsBuilder.fromHttpUrl(String
				.format("%s/api/v1/connectors/version/serviceCode/%s/variables/", connectorApiEndpoint, serviceCode))
				.build().toUriString();
		logger.debug("A2A Connector URL : {}", url);

		final ResponseEntity<Connector> exchange = restTemplate.exchange(url, HttpMethod.GET, getHttpEntity(),
				Connector.class);

		if (exchange == null || exchange.getBody() == null) {
			logger.warn("Internal Server issue. Please escalte to admin");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Internal Server issue. Please escalate to PD team.");
		}

		final Connector connector = exchange.getBody();
		return connector;
	}

	private HttpEntity<?> getHttpEntity() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return new HttpEntity<>(null, headers);
	}
}
