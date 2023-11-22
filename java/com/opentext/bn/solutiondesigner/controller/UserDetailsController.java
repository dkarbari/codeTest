package com.opentext.bn.solutiondesigner.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opentext.bn.solutiondesigner.exception.LdapTemplateException;
import com.opentext.bn.solutiondesigner.service.UserDetailsService;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

@RestController
@RequestMapping(value = "/api/v1")
public class UserDetailsController extends BaseController {
	final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

	
	
	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * 
	 * @param authentication
	 * @param request
	 * @param session
	 * @param userId
	 * @param extraAttributes
	 * @return
	 */
	@RequestMapping(value = { "/userDetails/{userId}" }, method = RequestMethod.GET)
	public ResponseEntity<?> roles(Authentication authentication, HttpServletRequest request, HttpSession session,
			final @PathVariable String userId,
			@RequestParam(required = false, defaultValue = "") String extraAttributes) {
		UserDetails userDetails = null;
		try {
			userDetails = userDetailsService.getUserDetailsById(userId, extraAttributes);
		} catch (LdapTemplateException e) {
			JSONObject json = new JSONObject();
			json.put("message", e.getMessage());
			return new ResponseEntity<String>(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);

	}

}
