package com.opentext.bn.solutiondesigner.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentext.bn.solutiondesigner.vo.RegisteredServiceListErrorResponse;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServices;
import com.opentext.bn.solutiondesigner.vo.serviceregistry.RegisteredServicesList;

public class TGAServiceUtils {

	final static Logger logger = LoggerFactory.getLogger(TGAServiceUtils.class);

	/*
	 * This method captures valid and invalid registered services. 
	 * This constructs error message which needs to be shown in the UI based on the violations specified. 
	 * Above information will be captured in RegisteredServiceListErrorResponse object. 
	 * 
	 * @param violations - Violations in registered services.
	 * @param registeredServicesList - List of registered services retrieved from CMD calls.
	 * @return RegisteredServiceListErrorResponse.
	 */
	public static RegisteredServiceListErrorResponse getValidAndInvalidServicesList(Set<ConstraintViolation<RegisteredServicesList>> violations,
			RegisteredServicesList registeredServicesList) {

		final StringBuffer rtnStringBuffer = new StringBuffer();

		List<RegisteredServices> validServices = new ArrayList<RegisteredServices>();
		List<RegisteredServices> invalidServices = new ArrayList<RegisteredServices>();

		for (int index = 0; index < registeredServicesList.getIm_services().size(); ++index) {
			RegisteredServices registeredServices = registeredServicesList.getIm_services().get(index);
			int imServicesIndex = -1;
			final Iterator<ConstraintViolation<RegisteredServicesList>> iterator = violations.iterator();
			boolean isValidationFailure = false;

			while (iterator.hasNext()) {
				ConstraintViolation<RegisteredServicesList> constraintViolation = iterator.next();
				for (Node node : constraintViolation.getPropertyPath()) {
					if (node.getIndex() != null) {
						imServicesIndex = node.getIndex(); // This gives index of im_services list which has violation
						break;
					}
				}
				if (index == imServicesIndex) {
					rtnStringBuffer
							.append("Service Name: " + registeredServices.getServiceInfo().getServiceName());
					rtnStringBuffer.append("\nInvalid Data: " + constraintViolation.getInvalidValue());
					rtnStringBuffer.append("\nError Message: " + constraintViolation.getMessage()).append("\n\n");
					isValidationFailure = true;
				}
			}

			if (!isValidationFailure) {
				validServices.add(registeredServices);
			} else {
				invalidServices.add(registeredServices);
			}
		}
		
		rtnStringBuffer.insert(0, constructInvalidServicesList(invalidServices));

		RegisteredServiceListErrorResponse serResponse = new RegisteredServiceListErrorResponse(rtnStringBuffer.toString(), 
																getRegisterService(validServices), getRegisterService(invalidServices));
		logger.debug("getValidAndInvalidServicesList: Valid/Invalid Registered services: {}", serResponse);

		return serResponse;
	}

	/*
	 * Returns RegisteredServicesList by setting servicesList to im_services property
	 *   
	 * @param servicesList
	 * @return RegisteredServicesList - sets services list to newly created RegisteredServicesList.
	 */
	private static RegisteredServicesList getRegisterService(List<RegisteredServices> servicesList) {
		RegisteredServicesList registerService = null;
		if (servicesList != null && servicesList.size() > 0) {
			registerService = new RegisteredServicesList();
			registerService.setIm_services(servicesList);
		}
		return registerService;
	}
	
	/*
	 * Captures list of Invalid Service Names from the services list.  
	 *  
	 * @param servicesList
	 * @return String - List of Service Names separated by new line. 
	 */
	private static String constructInvalidServicesList(List<RegisteredServices> servicesList) {
		StringBuffer rtnStringBuffer = new StringBuffer();
			for(RegisteredServices service: servicesList) {
				rtnStringBuffer.append(service.getServiceInfo().getServiceName()).append("\n");
			}
		return rtnStringBuffer.append("\n").toString();	
	}	
}
