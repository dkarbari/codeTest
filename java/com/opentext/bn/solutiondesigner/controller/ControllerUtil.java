package com.opentext.bn.solutiondesigner.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.Level;	       
import org.apache.logging.log4j.LogManager;	       
import org.apache.logging.log4j.Logger;
import com.opentext.bn.solutiondesigner.exception.LdapTemplateException;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

public class ControllerUtil {

	private static final Logger logger = LogManager.getLogger(ControllerUtil.class);
	/**
	 * Set cache headers for rest APIs.
	 */
	static public void setCacheHeaders(HttpHeaders httpHeaders) {
		HttpHeaders headers = httpHeaders;
		headers.set("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.set("Pragma", "no-cache");
		headers.set("Expires", "0");

	}

	public static void setRolesInSession(HttpSession session, LinkedList<String> roles) {
		session.setAttribute("ROLES", roles);
	}

	/**
	 * Will throw an exception when do not have role Its meant to be used by REST
	 * API to convert to an error
	 * 
	 * @param session
	 * @param role
	 * @throws ResponseStatusException
	 */
	public static void hasRoleInSession(HttpSession session, String role) throws ResponseStatusException {
		@SuppressWarnings("unchecked")
		LinkedList<String> roles = (LinkedList<String>) session.getAttribute("ROLES");

		if (roles == null || !roles.contains(role)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Do not have the role " + role);
		}

	}

	public static List<UserDetails> ldapSearch(String ldapUrls, String ldapUsername, String ldapPassword,
			String ldapUserSearchBase, String ldapUserSearchFilter, String ldapUserDisplayNameAttr,
			String ldapEmailAttr, String ldapAttribute, String name) throws LdapTemplateException {

		LdapContextSource contextSource = new LdapContextSource();

		contextSource.setUrl(ldapUrls);
		contextSource.setUserDn(ldapUsername);
		contextSource.setPassword(ldapPassword);
		contextSource.afterPropertiesSet();

		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);

		try {
			ldapTemplate.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("ldapSearch failed: " + ControllerUtil.getStackTrace(e));
			throw new LdapTemplateException(e.getMessage());
		}

		List<UserDetails> userDetailsList = ldapTemplate.search(ldapUserSearchBase,
				ldapUserSearchFilter.replace("{0}", name),
				new UserDetailsMapper(ldapUserDisplayNameAttr, ldapEmailAttr, ldapAttribute));

		return userDetailsList;
	}
	
	/*	       
	 * To audit important in custom log level information for debugging 	       
	 */	       
	public static void audit(String string) {	       
		logger.log(Level.getLevel("AUDIT"), string);	       
	}
	
	public static String getStackTrace(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		printWriter.close();
		return stringWriter.toString();
	}
}
