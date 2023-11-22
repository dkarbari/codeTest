package com.opentext.bn.solutiondesigner.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.opentext.bn.solutiondesigner.util.SwaggerConstant;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.ScriptCache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/scriptcaches")
@Api(tags = { "Script registration" })

public class ScriptCacheController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(ScriptCacheController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Validator validator;

	@Autowired
	private DareEnvironmentProperties dareEnvironmentProperties;

	@ApiOperation(value = "Register script in cache service")
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/javascript")
	@ApiResponses(value = { @ApiResponse(code = 200, response = ScriptCache.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ScriptCache registerScriptCache(
			@ApiParam(value = "Java script content", required = true, example = SwaggerConstant.SCRIPT_CACHE_SAMPLE)
			@RequestBody(required = true)
			final String scriptContent) {

		logger.info("Script content: {}", scriptContent);
		if (scriptContent == null || scriptContent.trim().equals("")) {
			logger.warn("Rececived invalid script content");
			throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, null, null,
					"Rececived invalid script content".getBytes(), null);
		}

		if (dareEnvironmentProperties.getScriptCacheRestBaseUrl() == null) {
			logger.error("Script cache ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}

		final String scriptId = UUID.randomUUID().toString();
		final String url = UriComponentsBuilder.fromHttpUrl(
				String.format("%sv3/scripts/%s/", dareEnvironmentProperties.getScriptCacheRestBaseUrl(), scriptId))
				.build().toUriString();
		logger.debug("Cache service ReST endpoint : {}", url);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/javascript"));
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		final ResponseEntity<ScriptCache> exchange = restTemplate.exchange(url, HttpMethod.POST,
				new HttpEntity<>(scriptContent, headers), ScriptCache.class);
		if (exchange == null) {
			logger.error(
					"exchange() is never supposed to return null, but in case something changes in the future we are detecting for completeness");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while registering script cache. Please try after some time. If this issue continues, please escalate to operations");
		}

		final ScriptCache rntScriptCache = exchange.getBody();
		final Set<ConstraintViolation<ScriptCache>> violations = validator.validate(rntScriptCache);
		if (violations.size() != 0) {
			logger.warn("Response received from cache service is improper: {}", violations);
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					getViolationMessage(violations).getBytes(), null);
		}

		rntScriptCache.setScriptId(scriptId);
		return rntScriptCache;
	}

	private String getViolationMessage(final Set<ConstraintViolation<ScriptCache>> violations) {
		final StringBuffer rtnStringBuffer = new StringBuffer(
				"Issue while receiving a response after registering itinerary");
		final Iterator<ConstraintViolation<ScriptCache>> iterator = violations.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation<ScriptCache> constraintViolation = iterator.next();
			rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
		}
		return rtnStringBuffer.toString();
	}

}