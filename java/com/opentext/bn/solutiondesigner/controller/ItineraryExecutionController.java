package com.opentext.bn.solutiondesigner.controller;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.opentext.bn.solutiondesigner.util.SwaggerConstant;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.ItineraryExecution;
import com.opentext.bn.solutiondesigner.vo.itinerary.process.ProcessRequest;
import com.opentext.bn.solutiondesigner.vo.itinerary.process.ProcessRoot;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/v1/itineraryprocesses")
@Api(tags = { "Itinerary execution" })

public class ItineraryExecutionController extends BaseController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private Validator validator;

	@Autowired
	private DareEnvironmentProperties dareEnvironmentProperties;

	private final Logger logger = LoggerFactory.getLogger(ItineraryExecutionController.class);
	private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@ApiOperation(value = "Launch itinerary using JSON specified by process API")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, response = ItineraryExecution.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public ItineraryExecution createItineraryProcess(
			@ApiParam(value = SwaggerConstant.ITINERARY_SERVICE_DESCRIPTION, required = true, example = SwaggerConstant.ITINERARY_INFORMATION)
			@Valid
			@RequestBody(required = true)
			final ProcessRequest processRequest) throws RestClientException, JsonProcessingException {

		if (dareEnvironmentProperties.getEngineAkkaHttpRestUrl() == null
				|| dareEnvironmentProperties.getEngineProcessesServicePath() == null) {
			logger.error("Itinerary process ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated ssue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}

		final String url = UriComponentsBuilder
				.fromHttpUrl(String.format("%s%s?wait=false", dareEnvironmentProperties.getEngineAkkaHttpRestUrl(),
						dareEnvironmentProperties.getEngineProcessesServicePath()))
				.build().toUriString();
		logger.info("Itinerary process ReST endpoint: {}", url);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("x-ottg-caller-application", "Solution Designer");
		headers.set("x-ottg-caller-application-host", "");
		headers.set("x-ottg-caller-application-timestamp", dateFormat.format(Calendar.getInstance().getTime()));
		logger.debug("Created Http Headers");

		final ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST,
				new HttpEntity<>(generateProcessRoot(processRequest), headers), String.class);

		if (exchange == null) {
			logger.error(
					"exchange() is never supposed to return null, but in case something changes in the future we are detecting for completeness");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while executing itinerary. Please try after some time. If the issue continues, please escalate");
		}
		logger.debug("Invoked itinerary successfully.");

		final ItineraryExecution itineraryExecution = new ObjectMapper().readValue(exchange.getBody(),
				ItineraryExecution.class);
		final Set<ConstraintViolation<ItineraryExecution>> violations = validator.validate(itineraryExecution);
		if (violations.size() != 0) {
			logger.error("Response received from itinerary process api is improper: {}", violations);
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					getViolationMessage(violations).getBytes(), null);
		}

		return itineraryExecution;
	}

	private String generateProcessRoot(final ProcessRequest processRequest) throws JsonProcessingException {
		final ProcessRoot processRoot = generateSampleProcess();
		updateProcessRoot(processRoot, processRequest);

		final String itineraryExecutionJSON = new ObjectMapper().writeValueAsString(processRoot);
		logger.info("Itinerary defintion JSON : {}", itineraryExecutionJSON);

		return itineraryExecutionJSON;
	}

	private ProcessRoot generateSampleProcess() throws HttpServerErrorException {

		try {
			final Resource resource = resourceLoader.getResource("classpath:samples/itinerary-process.json");
			return new ObjectMapper().readValue(resource.getInputStream(), ProcessRoot.class);
		} catch (Throwable eGenThrowable) {
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Unable to load sample JSON file".getBytes(), null);
		}
	}

	private String getViolationMessage(final Set<ConstraintViolation<ItineraryExecution>> violations) {
		final StringBuffer rtnStringBuffer = new StringBuffer(
				"Issue while receiving a response after registering itinerary");
		final Iterator<ConstraintViolation<ItineraryExecution>> iterator = violations.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation<ItineraryExecution> constraintViolation = iterator.next();
			rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
		}
		return rtnStringBuffer.toString();
	}

	private void updateProcessRoot(final ProcessRoot processRoot, final ProcessRequest processRequest) {

		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setSnrf(processRequest.getProcessUserInputs().getSnrf());
		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setSender(processRequest.getProcessUserInputs().getSender());
		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setDocType(processRequest.getProcessUserInputs().getDocType());
		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setReceiver(processRequest.getProcessUserInputs().getReceiver());
		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setDataType(processRequest.getProcessUserInputs().getDataType());
		processRoot.getInitiateItineraryRequest().getProcessMetaData()
				.setSolutionId(processRequest.getProcessUserInputs().getSolutionId());

		processRoot.getInitiateItineraryRequest().getItineraryRequest()
				.setItineraryName(processRequest.getItineraryId());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload().getPayloadMetaData()
				.setSnrf(processRequest.getProcessUserInputs().getSnrf());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload().getPayloadMetaData()
				.setSender(processRequest.getProcessUserInputs().getSender());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload().getPayloadMetaData()
				.setDocType(processRequest.getProcessUserInputs().getDocType());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload().getPayloadMetaData()
				.setReceiver(processRequest.getProcessUserInputs().getReceiver());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload().getPayloadMetaData()
				.setDataType(processRequest.getProcessUserInputs().getDataType());

		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload()
				.setPayloadKey(processRequest.getProcessUserInputs().getPayloadKey());
		processRoot.getInitiateItineraryRequest().getItineraryRequest().getInputPayload()
				.setPayloadUri(processRequest.getProcessUserInputs().getPayloadUri());
		processRoot.getInitiateItineraryRequest().getHeader().setProcessId(UUID.randomUUID().toString());
	}
}
