package com.opentext.bn.solutiondesigner.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.opentext.bn.solutiondesigner.vo.ServiceRequestVO;
import com.opentext.bn.solutiondesigner.vo.ServiceResponseVO;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServices;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServicesList;

@Component
public class ServiceRegistryTask extends CustomRunnable implements EnvironmentAware {

	final Logger logger = LoggerFactory.getLogger(ServiceRegistryTask.class);
	private static Environment APPLICATION_ENVIRONMENT = null;
	@Autowired
	private DareEnvironmentProperties dareEnvironmentProperties;
	public final String restEndPoint="/v3/registeredServices";


	public void run() {

		final HttpHeaders httpHeaders = createHttpHeader();

		final ServiceRequestVO serviceRequestVO = (ServiceRequestVO) getRequestVO();
		final ServiceResponseVO serviceResponseVO = (ServiceResponseVO) getResponseVO();
		final Iterator<String> serviceCodeIterator = serviceRequestVO.getServiceCodeList().iterator();

		while (serviceCodeIterator.hasNext()) {

			final String serviceCode = serviceCodeIterator.next();

			final RestTemplate restTemplate = new RestTemplate();

			final String endpointURL = UriComponentsBuilder.fromHttpUrl(String.format("%s/%s",
					dareEnvironmentProperties.getCmdRestUrl()+restEndPoint, serviceCode))
					.build().toUriString();
			logger.debug("CMD ReST endpoint : {}", endpointURL);

			final ResponseEntity<RegisteredServices> exchange = restTemplate.exchange(endpointURL, HttpMethod.GET,
					new HttpEntity<>("", httpHeaders), RegisteredServices.class);
			if (exchange == null || exchange.getBody() == null) {
				logger.warn("Internal Server issue. Please escalte to admin");
				continue;
			}
			logger.debug("Scheduled task retrieved service list successfully from CMD");

			final List<RegisteredServices> lstRegisteredServices = new ArrayList<RegisteredServices>();
			lstRegisteredServices.add(exchange.getBody());

			final RegisteredServicesList registeredServicesList = new RegisteredServicesList();
			registeredServicesList.setIm_services(lstRegisteredServices);

			serviceResponseVO.getServiceCodeResponse().put(exchange.getBody().getServiceInfo().getServiceCode(),
					registeredServicesList);
		}
	}

	@Override
	public void setEnvironment(final Environment environment) {
		ServiceRegistryTask.APPLICATION_ENVIRONMENT = environment;

	}

	private HttpHeaders createHttpHeader() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("im_service_instance_id",
				APPLICATION_ENVIRONMENT.getProperty("solution.designer.registered.services.im_service_instance_id"));
		httpHeaders.set("im_community_id",
				APPLICATION_ENVIRONMENT.getProperty("solution.designer.registered.services.im_community_id"));
		httpHeaders.set("im_user_id",
				APPLICATION_ENVIRONMENT.getProperty("solution.designer.registered.services.im_user_id"));
		httpHeaders.set("im_bu_id",
				APPLICATION_ENVIRONMENT.getProperty("solution.designer.registered.services.im_bu_id"));
		httpHeaders.set("im_principal_type",
				APPLICATION_ENVIRONMENT.getProperty("solution.designer.registered.services.im_principal_type"));
		return httpHeaders;
	}
}
