/***
 * Validating Process Definition JSON.
 * 
 */
package com.opentext.bn.solutiondesigner.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gxs.dare.orch.dto.ipd.ProcessDefinition;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/v1/itineraryValidations")
public class ItineraryValidations extends BaseController {
	final Logger logger = LoggerFactory.getLogger(ItineraryValidations.class);

	/**
	 * This method calls the ProcessDefinition's validate method to validate Process
	 * Definition to check Path, Task and Branch Looping validations Example:
	 * http://localhost:8080/api/v1/itineraryValidations
	 * 
	 * @param processDefinition - Newly created itinerary definition json string.
	 * @return respaoceJSON - JSON string with validation message and validation
	 *         status.
	 * @throws IOException
	 */
	@ApiOperation(value = "(Roles=ITINERARY_DEVELOPER)")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> validateItinerary(HttpSession session, final @RequestBody
	String processDefinition) throws IOException {
		JSONObject json = new JSONObject();
		try {
			if (processDefinition != null && processDefinition.length() > 0) {
				ProcessDefinition def = ProcessDefinition.getInstanceFromJson(processDefinition);
				logger.info("Calling - Process Definition validate method");
				def.validate();
			} else {
				logger.info("Invalid Process Definition - Process Definition is empty ");
			}

		} catch (UnsupportedEncodingException e) {
			json.put("validationError", Boolean.TRUE);
			json.put("message", e.getMessage());
			logger.info("UnsupportedEncodingException", e.getMessage());
			return new ResponseEntity<String>(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalArgumentException ie) {
			json.put("validationError", Boolean.TRUE);
			json.put("message", ie.getMessage());
			logger.info("Invalid Process Definition ", ie.getMessage());
			return new ResponseEntity<String>(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Success scenario
		json.put("validationError", Boolean.FALSE);
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

}
