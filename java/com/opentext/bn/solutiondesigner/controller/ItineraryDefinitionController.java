package com.opentext.bn.solutiondesigner.controller;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.opentext.bn.solutiondesigner.util.SwaggerConstant;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.ItineraryDefinition;
import com.opentext.bn.solutiondesigner.vo.itinerary.definition.DefinitionRequest;
import com.opentext.bn.solutiondesigner.vo.itinerary.definition.DefinitionRoot;
import com.opentext.bn.solutiondesigner.vo.itinerary.definition.Task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/itinerarydefinitions")
@Api(tags = { "Itinerary registration" })

public class ItineraryDefinitionController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private Validator validator;

	@Autowired
	private DareEnvironmentProperties dareEnvironmentProperties;

	private final Logger logger = LoggerFactory.getLogger(ItineraryDefinitionController.class);

	@ApiOperation(value = "Register temporary itinerary using defintion API")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = ItineraryDefinition.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ItineraryDefinition createItineraryDefinition(
			@ApiParam(value = SwaggerConstant.SCRIPT_SERVICE_DESCRIPTION, required = true, example = SwaggerConstant.SCRIPT_SERVICE_INFORMATION)
			@Valid
			@RequestBody(required = true)
			final DefinitionRequest definitionRequest) throws JsonProcessingException {

		final String itineraryId = UUID.randomUUID().toString();

		logger.info("Definition input received: {}", definitionRequest);

		if (dareEnvironmentProperties.getEngineHttpLbEndPoint() == null
				|| dareEnvironmentProperties.getEngineItineraryDeploymentUrl() == null) {
			logger.error("Itinerary defintion ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated this issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}

		final String url = UriComponentsBuilder
				.fromHttpUrl(String.format("%s%s%s", dareEnvironmentProperties.getEngineHttpLbEndPoint(),
						dareEnvironmentProperties.getEngineItineraryDeploymentUrl(), itineraryId))
				.build().toUriString();

		logger.debug("Itinerary defintion ReST endpoint : {}", url);

		final ResponseEntity<ItineraryDefinition> exchange = restTemplate.exchange(url, HttpMethod.POST,
				getMultiPartHttpEntity(generateDefinitionRoot(definitionRequest, itineraryId), itineraryId),
				ItineraryDefinition.class);

		if (exchange == null) {
			logger.error(
					"exchange() is never supposed to return null, but in case something changes in the future we are detecting for completeness");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while registering itinerary definition. Please try after some time. If this issue continues, please escalate to operations");
		}

		logger.debug("Registered itinerary definition successfully.");

		final ItineraryDefinition itineraryDefinition = exchange.getBody();
		itineraryDefinition.setItineraryId(itineraryId);
		validateItineraryDefinitionResponse(itineraryDefinition);
		return itineraryDefinition;
	}

	private String generateDefinitionRoot(final DefinitionRequest definitionRequest, final String itinerrayName)
			throws JsonProcessingException {
		final DefinitionRoot definitionRoot = generateSampleDefintion();
		updateDefinitionRoot(definitionRoot, definitionRequest);
		definitionRoot.getItinerary().setName(itinerrayName);

		final String itineraryDefintionJSON = new ObjectMapper().writeValueAsString(definitionRoot);
		logger.info("Itinerary defintion JSON : {}", itineraryDefintionJSON);

		return itineraryDefintionJSON;
	}

	private DefinitionRoot generateSampleDefintion() throws HttpServerErrorException {

		try {
			final Resource resource = resourceLoader.getResource("classpath:samples/itinerary-definition.json");
			return new ObjectMapper().readValue(resource.getInputStream(), DefinitionRoot.class);
		} catch (Throwable eGenThrowable) {
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Unable to load sample JSON file".getBytes(), null);
		}
	}

	private HttpEntity<?> getMultiPartHttpEntity(final String itineraryContent, final String itineraryName) {

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		final MultiValueMap<String, String> fileMultiValueMap = new LinkedMultiValueMap<>();
		final ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
				.filename(itineraryName + ".oid").build();
		fileMultiValueMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

		final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new HttpEntity<>(itineraryContent.getBytes(), fileMultiValueMap));
		body.add("itineraryVersion", "2.0");

		return new HttpEntity<>(body, headers);
	}

	private void validateItineraryDefinitionResponse(final ItineraryDefinition itineraryDefinition) {

		final Set<ConstraintViolation<ItineraryDefinition>> violations = validator.validate(itineraryDefinition);
		if (violations.size() == 0) {
			return;
		}

		final StringBuffer rtnStringBuffer = new StringBuffer(
				"Issue while receiving a response after registering itinerary");
		final Iterator<ConstraintViolation<ItineraryDefinition>> iterator = violations.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation<ItineraryDefinition> constraintViolation = iterator.next();
			rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
		}

		logger.error("Response received from itinerary defintion api is improper: {}", violations);
		throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
				rtnStringBuffer.toString().getBytes(), null);
	}

	private void updateDefinitionRoot(final DefinitionRoot definitionRoot, final DefinitionRequest definitionRequest) {

		for (Task task : definitionRoot.getItinerary().getPaths().get(0).getTasks()) {

			if (task.getServiceCode().equals("startTask")) {
				task.setNextTaskInstanceId(definitionRequest.getScriptId());
			}

			if (task.getServiceCode().equals("ScriptService")) {
				task.setTaskInstanceId(definitionRequest.getScriptId());
				task.setTaskName(definitionRequest.getTaskName());
				task.setServiceCode(definitionRequest.getServiceCode());
				task.setTaskEmbeddedProperties(definitionRequest.getTaskEmbeddedProperties());
			}
		}
	}
}