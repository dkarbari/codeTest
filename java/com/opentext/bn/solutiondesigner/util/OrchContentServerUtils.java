package com.opentext.bn.solutiondesigner.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.gxs.dare.common.environment.DareEnvironmentProperties;
import com.gxs.orch.rest.client.OrchRestException;
import com.gxs.orch.rest.client.contentserver.OrchContentServerRestClient;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class OrchContentServerUtils {

	private static final Logger logger = LoggerFactory.getLogger(OrchContentServerUtils.class);
	protected static OrchContentServerUtils _instance = new OrchContentServerUtils();

	public final static String OTDS_TOKEN = "x-otds-token";
	final static String KEY_USERNAME = "username";
	final static String KEY_SECRET = "secret";

	/**
	 * @return <code>OrchContentServerUtils</code> instance
	 */
	public static OrchContentServerUtils getInstance() {
		return _instance;
	}

	protected OrchContentServerRestClient createOrchContentServerRestClient() {
		return new OrchContentServerRestClient();
	}

	/***
	 * 
	 * Customize the JSON data returned from content server.
	 * 
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	public static String customizeJSONData(final String jsonString) throws JSONException {
		final JSONObject returnJsonObject = new JSONObject();
		final JSONArray returnJsonArray = new JSONArray();
		returnJsonObject.put("data", returnJsonArray);

		final JSONObject jsonObject = new JSONObject(jsonString);
		final JSONArray jsonArray = jsonObject.getJSONArray("data");

		for (int iCount = 0; iCount < jsonArray.length(); iCount++) {
			final JSONObject tempObject = new JSONObject();

			tempObject.put("id", jsonArray.getJSONObject(iCount).getInt("id"));
			tempObject.put("name", jsonArray.getJSONObject(iCount).getString("name"));
			tempObject.put("owner", jsonArray.getJSONObject(iCount).getString("owner"));
			tempObject.put("summary", jsonArray.getJSONObject(iCount).getString("summary"));
			tempObject.put("type_name", jsonArray.getJSONObject(iCount).getString("type_name"));
			tempObject.put("modify_date", jsonArray.getJSONObject(iCount).getString("modify_date"));
			returnJsonArray.put(tempObject);
		}

		return returnJsonObject.toString();
	}

	/***
	 * 
	 * Parent information is identified as follows:
	 * 
	 * 1. If no parent, will return personal folder information. 2. If mentioned as
	 * "PERSONAL", will return personal folder information. 3. If mentioned as
	 * Shared, will return the configured folder ID 4. For other, will retrieve the
	 * folder from content server using the value mentioned.
	 * 
	 * @param orchContentServerRestClient
	 * @param parent
	 * @param otdsToken
	 * @return
	 * @throws OrchRestException
	 * @throws JSONException
	 * @throws ResponseStatusException
	 */
	public static int getParentID(final OrchContentServerRestClient orchContentServerRestClient, final String parent,
			final String otdsToken) throws ResponseStatusException, JSONException, OrchRestException {

		if (parent == null) {
			return orchContentServerRestClient.validateToken(otdsToken);
		}

		logger.info("Parent ID passed by user is: {}", parent);

		if (parent.equalsIgnoreCase("PERSONAL")) {
			return orchContentServerRestClient.validateToken(otdsToken);
		}

		final String solutionSpace = parent.equalsIgnoreCase("SHARED")
				? DareEnvironmentProperties.getInstance().getContentServerCommonSolutionId()
				: parent;

		final int parentID = orchContentServerRestClient.getNodeId(
				Integer.parseInt(DareEnvironmentProperties.getInstance().getOtLocationNode()), solutionSpace);
		if (parentID <= 0) {
			logger.warn("Solution ID: {}, does not exist in Content Server", solutionSpace);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solution ID not found");
		}

		final int itineraryID = orchContentServerRestClient.getNodeId(parentID,
				DareEnvironmentProperties.getInstance().getContentServerItineraryFolderName());
		if (itineraryID <= 0) {
			logger.warn("Solution ID: {}, does not have ITINERARIES folder", parent);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solution does not have ITINERARIES folder");
		}

		return itineraryID;
	}

	/***
	 *
	 * This method verifies if the OTDS Token (that we keep in the Tomcat session)
	 * is still valid. If the OTDS Token is expired or invalid, will retrieve a new
	 * token.
	 */
	public static String getTokenFromSession(HttpSession session, boolean ldapEnabled) throws ResponseStatusException {
		logger.debug("getTokenFromSession: attempting to validate OTDS");

		// Retrieve the current token from the session
		String otdsTokenFromSession = (String) session.getAttribute(OTDS_TOKEN);

		// Ensure that we use a new OrchContentServerRestClient instance, do not pass in
		// the caller's instance of it.
		// The OrchContentServerRestClient instance caches the request headers and may
		// fail because of it.
		final OrchContentServerRestClient orchContentServerRestClient = getInstance().createOrchContentServerRestClient();

		// Solution Designer Service will handle re-acquiring expired tokens (set the
		// flag to tell CSRestClient not to re-acquire token).
		orchContentServerRestClient.setShouldReAcquireTokenWhenExpired(false);

		try {
			if (otdsTokenFromSession == null)
				session.setAttribute(OTDS_TOKEN, getNewToken(ldapEnabled));
			else
				orchContentServerRestClient.validateToken(otdsTokenFromSession);
		} catch (OrchRestException oex) {
			// For now we will treat all exceptions as an invalid or expired token
			logger.warn("getTokenFromSession: validateToken failed, error: {} \n Will attempt to get new token",
					oex.getMessage());
			session.setAttribute(OTDS_TOKEN, getNewToken(ldapEnabled));
		} catch (JSONException jsonex) {
			// For now we will treat all exceptions as an invalid or expired token
			logger.warn("getTokenFromSession: validateToken failed, error: {} \n Will attempt to get new token",
					jsonex.getMessage());
			session.setAttribute(OTDS_TOKEN, getNewToken(ldapEnabled));
		}

		logger.debug("getTokenFromSession: successfully validated token");

		// Return the token from the session attribute (it should of been validated or
		// renewed at this point).
		return (String) session.getAttribute(OTDS_TOKEN);
	}

	public static String getNewToken(boolean ldapEnabled) throws ResponseStatusException {
		String loginResponse = "";

		try {

			// Ensure that we use a new OrchContentServerRestClient instance, do not pass in
			// the caller's instance of it.
			// The OrchContentServerRestClient instance caches the request headers and may
			// fail because of it.
			final OrchContentServerRestClient orchContentServerRestClient = getInstance().createOrchContentServerRestClient();

			// Solution Designer Service will handle re-acquiring expired tokens (set the
			// flag to tell CSRestClient not to re-acquire token).
			orchContentServerRestClient.setShouldReAcquireTokenWhenExpired(false);

			// Check if we are using custom credentials (only if ldapEnabled) or shared
			// credentials (from Properties)
			Map<String, String> userCrendentials = null;
			if (ldapEnabled) {
			
				userCrendentials = getCustomCredentialsFromSecurityContext(ldapEnabled);
				
				if (userCrendentials != null)
					loginResponse = orchContentServerRestClient.login(userCrendentials.get(KEY_USERNAME),
							userCrendentials.get(KEY_SECRET));
			}
			else {
				// ldap is not enabled, use shared credentials
				loginResponse = orchContentServerRestClient.login(
						DareEnvironmentProperties.getInstance().getContentServerUserName(),
						DareEnvironmentProperties.getInstance().getContentServerPassword());
			}
			
		} catch (OrchRestException oex) {
			logger.error("GetNewToken failed: " + oex.getMessage());
			String msg = oex.getResponseBodyAsString();
			if (msg == null)
				msg = oex.getMessage();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
		} catch (JSONException jsonex) {
			logger.error("GetNewToken failed: " + jsonex.getMessage());
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, jsonex.getMessage());
		}

		if (loginResponse == null || loginResponse.trim().equals("")) {
			logger.error("GetNewToken failed: Invalid Credentials");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
		}

		logger.debug("Successfully retrieved OTDS token. Response is: {} ", loginResponse);
		return loginResponse;
	}

	public static OrchContentServerRestClient getNewContentServerRestClient(HttpSession session, boolean ldapEnabled) {

		// Retrieve the token using this method, it will ensure that we have a valid
		// token
		String otdsTokenFromSession = getTokenFromSession(session, ldapEnabled);

		OrchContentServerRestClient orchContentServerRestClient = getInstance().createOrchContentServerRestClient();

		// Solution Designer Service will handle re-acquiring expired tokens (set the
		// flag to tell CSRestClient not to re-acquire token).
		orchContentServerRestClient.setShouldReAcquireTokenWhenExpired(false);
		orchContentServerRestClient.setContentServerOTDSToken(otdsTokenFromSession);

		return orchContentServerRestClient;
	}

	// This method determines whether we should use Custom credentials for Content
	// Server (using LDAP authentication with synchronized CS partition)
	// Note: When we are ldapEnabled it is assumed that the Content Server partition
	// will be configured to be synchronized with the LDAP account.
	private static Map<String, String> getCustomCredentialsFromSecurityContext(boolean ldapEnabled) {

		// Check if we are ldapEnabled, if so then the LDAP credentials should match
		// Content Server's
		if (ldapEnabled) {

			logger.debug(
					"getCustomCredentialsFromSecurityContext() ldapEnabled is true, will retrieve ldap credentials from Security Context");

			// retrieve the username portion
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || auth.getPrincipal() == null) {
				logger.error(
						"getCustomCredentialsFromSecurityContext failed: ldap is enabled but cannot get principal");
				return null;
			}
			
			Object principal = auth.getPrincipal();			
			String username = "";
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				org.springframework.security.core.userdetails.UserDetails userDetailsPrincipal = ((org.springframework.security.core.userdetails.UserDetails) principal);

				username = userDetailsPrincipal.getUsername();
			} else
				logger.error(
						"getCustomCredentialsFromSecurityContext failed: ldap is enabled but the getPrincipal() object is not an instance of org.springframework.security.core.userdetails.UserDetails");

			if (!StringUtils.isNotEmpty(username)) {
				logger.error(
						"getCustomCredentialsFromSecurityContext failed: ldap is enabled but the username is empty");
				return null;
			}

			Object credentials = auth.getCredentials();
			if (credentials == null || !(credentials instanceof String)) {
				logger.error(
						"getCustomCredentialsFromSecurityContext failed: ldap is enabled but the credentials is empty");
				return null;
			}
			Map<String, String> userCredential = new HashMap<String, String>();
			userCredential.put(KEY_USERNAME, username);
			userCredential.put(KEY_SECRET, (String) credentials);

			logger.debug("getCustomCredentialsFromSecurityContext() succesfully retrieved ldap credentials");
			return userCredential;
		} else
			logger.debug(
					"getCustomCredentialsFromSecurityContext() ldap is not enabled, will use shared credentials from properties");

		return null;
	}
}