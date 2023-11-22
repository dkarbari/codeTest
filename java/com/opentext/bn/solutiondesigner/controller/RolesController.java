package com.opentext.bn.solutiondesigner.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opentext.bn.solutiondesigner.exception.LdapTemplateException;
import com.opentext.bn.solutiondesigner.util.SolutionDesignerConstant;
import com.opentext.bn.solutiondesigner.vo.RolesResponse;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

@RestController
@RequestMapping(value = "/api/v1")
public class RolesController extends BaseController  {
	final Logger logger = LoggerFactory.getLogger(RolesController.class);
	private static final String[] LDAP_DISABLED_ROLES_MAP =
			"R_BN_ITINERARY_DEV=ITINERARY_DEVELOPER,R_BN_SERVICE_DEV=SERVICE_DEVELOPER".split(",");
	@Value("${ldap.roles.map:}")
	private String rolesMap;

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	@Value("${ldap.connectionURL}")
	private String ldapUrls;

	@Value("${ldap.connectionName}")
	private String ldapUsername;

	@Value("${ldap.connectionPassword}")
	private String ldapPassword;

	@Value("${ldap.userSearch}")
	private String ldapUserSearchFilter;

	@Value("${ldap.userBase}")
	private String ldapUserSearchBase;

	@Value("${ldap.user.group.filter}")
	private String ldapUserGroupFilter;

	/**
	 * 
	 * @param authentication
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/roles" }, method = RequestMethod.GET)
	public ResponseEntity<?> roles(Authentication authentication, HttpServletRequest request, HttpSession session) {

		HttpHeaders headers = new HttpHeaders();
		ControllerUtil.setCacheHeaders(headers);

		LinkedList<String> roles = new LinkedList<String>();
		RolesResponse rolesResponse = new RolesResponse();
		rolesResponse.setRoles(roles);

		// The following is for LDAP "memberOf" Group attributes
		List<Map<String, String>> userGroupAttributesList = null;
		String username = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
			org.springframework.security.core.userdetails.UserDetails userDetailsPrincipal = ((org.springframework.security.core.userdetails.UserDetails) principal);
			username = userDetailsPrincipal.getUsername();
		}

		if (StringUtils.isEmpty(username)) {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}

		// Check if we are LDAP enabled, if so then we need to use our own method
		// (non-Spring) to determine the user's roles
		if (Boolean.parseBoolean(ldapEnabled)) {
			// Retrieve the user's "memberOf" attributes once (this saves us from making
			// multiple LDAP connections)
			try {
				userGroupAttributesList = getUserGroupAttributes(ldapUserGroupFilter,username);
			} catch (LdapTemplateException e) {

				ControllerUtil.setRolesInSession(session, roles);

				JSONObject json = new JSONObject();
				json.put("message", e.getMessage());
				return new ResponseEntity<String>(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		String[] strArray = (Boolean.parseBoolean(ldapEnabled) ? rolesMap.split(",") : LDAP_DISABLED_ROLES_MAP);

		for (String pair : strArray) {
			String split[] = pair.split("=");

			boolean inRole = false;

			// Check if we are LDAP enabled, if so then we need to use our own method
			// (non-Spring) to determine the user's roles
			if (Boolean.parseBoolean(ldapEnabled)) {

				inRole = isUserInRole(split[0].trim(), userGroupAttributesList);
			} else
				inRole = request.isUserInRole(split[0].trim());

			if (inRole) {
				if (SolutionDesignerConstant.KEY_ROLES_MAP.containsKey(split[1].trim())) {
					roles.add(SolutionDesignerConstant.KEY_ROLES_MAP.get(split[1].trim()));
				} else {
					roles.add(split[1].trim());
				}
			}
		}
		if(!CollectionUtils.isEmpty(roles))
		ControllerUtil.audit(username+" having role "+roles+" logged in to SolutionDesigner successfully");
		ControllerUtil.setRolesInSession(session, roles);
		return new ResponseEntity<RolesResponse>(rolesResponse, HttpStatus.OK);
	}

	// This will determine if the user is a member of a particular group (role).
	// It retrieve the "memberOf" attributes for a user.
	public List<Map<String, String>> getUserGroupAttributes(String ldapUserGroupFilter,String username) throws LdapTemplateException {
		List<Map<String, String>> userGroupAttributesList = null;

		List<UserDetails> userDetailsList = ControllerUtil.ldapSearch(ldapUrls, ldapUsername, ldapPassword,
				ldapUserSearchBase, ldapUserSearchFilter, "", "", ldapUserGroupFilter, username);

		if (userDetailsList != null && userDetailsList.size() > 0) {

			userGroupAttributesList = new ArrayList<Map<String, String>>();

			for (UserDetails userDetails : userDetailsList) {
				userGroupAttributesList.add(userDetails.getAttrMap());
			}
		}

		return userGroupAttributesList;
	}

	// This method will determine if a given role exists in the GroupAttributesList
	private boolean isUserInRole(String role, List<Map<String, String>> userGroupAttributesList) {

		if (StringUtils.isEmpty(role))
			return false;

		for (Map<String, String> attributes : userGroupAttributesList) {

			for (String value : attributes.values()) {

				if (value.toLowerCase().indexOf(role.toLowerCase()) != -1)
					return true;
			}
		}

		return false;
	}



}
