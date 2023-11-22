package com.opentext.bn.solutiondesigner.controller;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.opentext.bn.solutiondesigner.exception.RegisteredServiceListInvalidException;
import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import com.opentext.bn.solutiondesigner.util.SwaggerConstant;
import com.opentext.bn.solutiondesigner.util.TGAServiceUtils;
import com.opentext.bn.solutiondesigner.vo.ErrorResponse;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.CMDResponse;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServices;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServicesList;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@RestController
@RequestMapping(value = "/api/v1/services")
@Api(tags = { "Service registry operations" })

public class TGAServiceRegistryController extends BaseController {
	@Value("${solution.designer.registered.services.im_bu_id}")
	private String imBuId;

	@Value("${solution.designer.registered.services.im_community_id}")
	private String imCommunityId;

	@Value("${solution.designer.registered.services.im_principal_type}")
	private String imPrincipalType;

	@Value("${solution.designer.registered.services.im_service_instance_id}")
	private String imServiceInstanceId;

	@Value("${solution.designer.registered.services.im_user_id}")
	private String imUserId;

	@Autowired
	private DareEnvironmentProperties dareEnvironmentProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Validator validator;

	final Logger logger = LoggerFactory.getLogger(TGAServiceRegistryController.class);
	public final String restEndPoint="/v3/registeredServices";
	
	
	@ApiOperation(value = "Create service in CMD")
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, response = CMDResponse.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public CMDResponse createCMDService(
			@ApiParam(value = SwaggerConstant.CREATE_SERVICE_DESCRIPTION, required = true, example = SwaggerConstant.CREATE_SERVICE_EXAMPLE)
			@Valid
			@RequestBody(required = true)
			final RegisteredServices registeredServices) {
	
		if (StringUtils.isEmpty(dareEnvironmentProperties.getCmdRestUrl())) {
			logger.error("CMD ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated this issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}
		String registeredServiceEndpoint=dareEnvironmentProperties.getCmdRestUrl() +restEndPoint ;
		logger.debug("CMD ReST endpoint : {}", registeredServiceEndpoint);

		final HttpHeaders httpHeaders = createHttpHeader();
		final ResponseEntity<CMDResponse> exchange = restTemplate.exchange(registeredServiceEndpoint, HttpMethod.POST,
				new HttpEntity<>(registeredServices, httpHeaders), CMDResponse.class);
		if (exchange == null || exchange.getBody() == null) {
			logger.error(
					"exchange() is never supposed to return null, but in case something changes in the future we are detecting for completeness");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while registering service. Please try after some time. If this issue continues, please escalate to operations");
		}

		logger.debug("Registered service successfully in CMD. Service Information:" + registeredServices.toString());

		final CMDResponse cmdResponse = exchange.getBody();
		final Set<ConstraintViolation<CMDResponse>> violations = validator.validate(cmdResponse);
		if (violations.size() != 0) {

			final StringBuffer rtnStringBuffer = new StringBuffer(
					"Issue while receiving a response after registering itinerary");
			final Iterator<ConstraintViolation<CMDResponse>> iterator = violations.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<CMDResponse> constraintViolation = iterator.next();
				rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
			}

			logger.error("Response received from itinerary defintion api is improper: {}", violations);
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					rtnStringBuffer.toString().getBytes(), null);
		}

		return cmdResponse;
	}

	@ApiOperation(value = "Search services")
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, response = RegisteredServicesList.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public RegisteredServicesList listRegisteredServices(
			@ApiParam(value = "Entry Type used while registering service", required = false, example = "orchestration_service")
			@RequestParam(required = false)
			@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_50, message = SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_50)
			final String entityType,

			@ApiParam(value = "Orchestration Type used while registring service", required = false, example = "orch-script")
			@RequestParam(required = false)
			@Pattern(regexp = SolutionDesignerConstant.GENERAL_REG_EXP_LEN_50, message = SolutionDesignerConstant.GENERAL_REG_EXP_MESSAGE_LEN_50)
			final String orchestrationType,

			@ApiParam(value = "Registered service to be searched", required = false, example = "ServiceNow")
			@RequestParam(required = false)
			@Pattern(regexp = SolutionDesignerConstant.SCODE_SEARCH_REG_EXP, message = SolutionDesignerConstant.SCODE_SEARCH_REG_EXP_MESSAGE)
			final String serviceName,

			@ApiParam(value = "Starting position of the records to return", required = false, example = "0")
			@RequestParam(required = false, defaultValue = "0")
			@Min(0)
			@Max(Integer.MAX_VALUE)
			final int after,

			@ApiParam(value = "Limit number of entries returned", required = false, example = "100")
			@RequestParam(required = false, defaultValue = "100")
			@Min(1)
			@Max(100)
			final int limit,

			@ApiParam(value = "Service code used registering service", required = false, example = "a2a-netsuite 1.0")
			@RequestParam(required = false)
			@Pattern(regexp = SolutionDesignerConstant.SCODE_SEARCH_REG_EXP, message = SolutionDesignerConstant.SCODE_SEARCH_REG_EXP_MESSAGE)
			final String serviceCode) throws RegisteredServiceListInvalidException

	{

		if (StringUtils.isEmpty(dareEnvironmentProperties.getCmdRestUrl())) {
			logger.error("CMD ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated this issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}
		
		final String endpointURL = constructCMDURL(entityType, orchestrationType, serviceName, serviceCode, after,
				limit);
		logger.debug("CMD ReST endpoint : {}", endpointURL);

		final HttpHeaders httpHeaders = createHttpHeader();
		final ResponseEntity<RegisteredServicesList> exchange = restTemplate.exchange(endpointURL, HttpMethod.GET,
				new HttpEntity<>("", httpHeaders), RegisteredServicesList.class);
		if (exchange == null || exchange.getBody() == null) {
			logger.warn("Internal Server issue. Please escalte to admin");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Internal Server issue. Please escalte to admin");
		}
		logger.debug("Retrieved service list successfully from CMD");
		
		if(entityType.equals("A2A")) {
			return exchange.getBody();
		}
		
		final RegisteredServicesList registeredServicesList = exchange.getBody();
		
		final Set<ConstraintViolation<RegisteredServicesList>> violations = validator.validate(registeredServicesList);
		
		if (violations.size() != 0) {
			logger.error("Response received from itinerary definition api is improper: {}, registeredServicesList: {} ", violations, registeredServicesList);
		    throw new RegisteredServiceListInvalidException(TGAServiceUtils.getValidAndInvalidServicesList(violations, registeredServicesList));
		}

		return registeredServicesList;
	}

	@ApiOperation(value = "Retrieve specific service using service id")
	@RequestMapping(value = { "/{serviceID}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, response = RegisteredServicesList.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public RegisteredServices retrieveServiceUsingID(
			@ApiParam(value = "Service code used registering service", required = true, example = "a2a-netsuite 1.0")
			@PathVariable(required = true)
			@NotNull(message = "Service ID is mandatory")
			@Pattern(regexp = SolutionDesignerConstant.CMD_SERVICE_REG_EXP, message = SolutionDesignerConstant.CMD_SERVICE_REG_EXP_MESSAGE)
			final String serviceID) {
		
		
		
		if (StringUtils.isEmpty(dareEnvironmentProperties.getCmdRestUrl())) {
			logger.error("CMD ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated this issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}
		String registeredServiceEndpoint=dareEnvironmentProperties.getCmdRestUrl() + restEndPoint;
		final String endpointURL = UriComponentsBuilder
				.fromHttpUrl(String.format("%s/%s", registeredServiceEndpoint, serviceID)).build().toUriString();
		logger.debug("CMD ReST endpoint : {}", endpointURL);

		final HttpHeaders httpHeaders = createHttpHeader();
		final ResponseEntity<RegisteredServices> exchange = restTemplate.exchange(endpointURL, HttpMethod.GET,
				new HttpEntity<>("", httpHeaders), RegisteredServices.class);
		if (exchange == null || exchange.getBody() == null) {
			logger.warn("Internal Server issue. Please escalte to admin");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Internal Server issue. Please escalte to admin");
		}
		logger.debug("Retrieved service list successfully from CMD");

		final RegisteredServices registeredServices = exchange.getBody();
		final Set<ConstraintViolation<RegisteredServices>> violations = validator.validate(registeredServices);
		if (violations.size() != 0) {

			final StringBuffer rtnStringBuffer = new StringBuffer(
					"Issue while receiving a response after registering itinerary");
			final Iterator<ConstraintViolation<RegisteredServices>> iterator = violations.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<RegisteredServices> constraintViolation = iterator.next();
				rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
			}

			logger.error("Response received from itinerary definition api is improper: {}, registeredServices: {} ", violations, registeredServices);
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					rtnStringBuffer.toString().getBytes(), null);

		}
		return registeredServices;
	}

	@ApiOperation(value = "Update service in CMD")
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, response = CMDResponse.class, message = "Success"),
			@ApiResponse(code = 400, response = ErrorResponse.class, message = "Bad Request"),
			@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error") })

	public CMDResponse updateCMDService(
			@ApiParam(value = SwaggerConstant.UPDATE_SERVICE_DESCRIPTION, required = true, example = SwaggerConstant.UPDATE_SERVICE_EXAMPLE)
			@Valid
			@RequestBody(required = true)
			final RegisteredServices registeredServices) {
		if (StringUtils.isEmpty(dareEnvironmentProperties.getCmdRestUrl())) {
			logger.error("CMD ReST endpoint properties are not configured correctly");
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					"Configuration issue. Escalated this issue to operations and will be resolved soon. Please try after some time."
							.getBytes(),
					null);
		}
		String registeredServiceEndpoint=dareEnvironmentProperties.getCmdRestUrl() +restEndPoint ;
		final String url = UriComponentsBuilder
				.fromHttpUrl(registeredServiceEndpoint + "/" + registeredServices.getServiceInstanceId()).build()
				.toUriString();

		logger.debug("CMD ReST endpoint : {}", url);

		final HttpHeaders httpHeaders = createHttpHeader();
		final ResponseEntity<CMDResponse> exchange = restTemplate.exchange(url, HttpMethod.PUT,
				new HttpEntity<>(registeredServices, httpHeaders), CMDResponse.class);
		if (exchange == null || exchange.getBody() == null) {
			logger.error(
					"exchange() is never supposed to return null, but in case something changes in the future we are detecting for completeness");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Issue while registering service. Please try after some time. If this issue continues, please escalate to operations");
		}
		logger.debug("Updated service successfully in CMD. Service Instance ID:"
				+ registeredServices.getServiceInstanceId());

		final CMDResponse cmdResponse = exchange.getBody();
		final Set<ConstraintViolation<CMDResponse>> violations = validator.validate(cmdResponse);
		if (violations.size() != 0) {

			final StringBuffer rtnStringBuffer = new StringBuffer(
					"Issue while receiving a response after registering itinerary");
			final Iterator<ConstraintViolation<CMDResponse>> iterator = violations.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<CMDResponse> constraintViolation = iterator.next();
				rtnStringBuffer.append(constraintViolation.getMessage() + "\r\n");
			}

			logger.error("Response received from itinerary defintion api is improper: {}", violations);
			throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, null, null,
					rtnStringBuffer.toString().getBytes(), null);
		}

		return cmdResponse;
	}

	private String constructCMDURL(final String entityType, final String orchestrationType, final String serviceName,
			final String serviceCode, final int after, final int limit) {
		
		String registeredServiceEndpoint=dareEnvironmentProperties.getCmdRestUrl() + restEndPoint;
		final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(registeredServiceEndpoint);

		if (serviceCode != null && (!(serviceCode.trim().equals("")))) {
			uriComponentsBuilder.queryParam("serviceCode", serviceCode);
		}

		if (serviceName != null && (!(serviceName.trim().equals("")))) {
			uriComponentsBuilder.queryParam("serviceName", serviceName);
		}

		if (orchestrationType != null && (!(orchestrationType.trim().equals("")))) {
			uriComponentsBuilder.queryParam("serviceType", orchestrationType);
		}

		if (entityType != null && (!(entityType.trim().equals("")))) {
			uriComponentsBuilder.queryParam("entryType", entityType);
		}

		uriComponentsBuilder.queryParam("after", after).queryParam("limit", limit);
		return uriComponentsBuilder.build().toString();
	}

	private HttpHeaders createHttpHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("im_service_instance_id", imServiceInstanceId);
		httpHeaders.set("im_community_id", imCommunityId);
		httpHeaders.set("im_user_id", imUserId);
		httpHeaders.set("im_bu_id", imBuId);
		httpHeaders.set("im_principal_type", imPrincipalType);
		return httpHeaders;
	}
}