package com.opentext.bn.solutiondesigner.controller;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.gxs.orch.rest.client.OrchRestException;

public class BaseController {
	
	final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	
	@ExceptionHandler(OrchRestException.class)
	public ResponseEntity<String> handleOrchRestException(final OrchRestException e) {
		logger.warn("OrchRestException handler, exception message: ",e);
   
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		try {
			status = HttpStatus.resolve(Integer.parseInt(e.getErrorCode()));
		}catch(IllegalArgumentException iae) { logger.info("Can't resolve the status code from the returned status: "+e.getErrorCode()); }

	    return new ResponseEntity<String>(e.getMessage(), e.getResponseHeaders(), status);
	}

	@ExceptionHandler({JSONException.class,IllegalArgumentException.class})
	public ResponseEntity<String> handleGenericException(final Exception e) {
		logger.warn("JSONException and IllegalArgumentException handler, exception message : ",e.getMessage());
	    return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<String> handleResponseStatusException(final ResponseStatusException e) {
		logger.error("ResponseStatusException handler, exception message : ",e);
	    return new ResponseEntity<String>(e.getMessage(), e.getResponseHeaders(), e.getStatus());
	}
		

}
