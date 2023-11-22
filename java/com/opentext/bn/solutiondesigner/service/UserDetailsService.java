package com.opentext.bn.solutiondesigner.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.opentext.bn.solutiondesigner.controller.ControllerUtil;
import com.opentext.bn.solutiondesigner.exception.LdapTemplateException;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

@Service
public class UserDetailsService {

	@Value("${ldap.connectionURL}")
	private String ldapUrls;

	@Value("${ldap.connectionName}")
	private String ldapUsername;

	@Value("${ldap.connectionPassword}")
	private String ldapPassword;

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	@Value("${ldap.userSearch}")
	private String ldapUserSearchFilter;

	@Value("${ldap.userBase}")
	private String ldapUserSearchBase;

	@Value("${ldap.user.displayName.attr}")
	private String ldapUserDisplayNameAttr;

	@Value("${ldap.user.mail.attr}")
	private String ldapUserMailAttr;

	public UserDetails getUserDetailsById(String userId, String extraAttributes) throws LdapTemplateException {
		UserDetails userDetails = null;

		if (Boolean.parseBoolean(ldapEnabled)) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = "";
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				org.springframework.security.core.userdetails.UserDetails userDetailsPrincipal = ((org.springframework.security.core.userdetails.UserDetails) principal);
				username = userDetailsPrincipal.getUsername();
			}
			if (StringUtils.isEmpty(username)) {
				username = userId;
			}

			// See if we using the hidden parameter for debugging LDAP problems
			String ldapAttribute = "";
			if (!StringUtils.isEmpty(extraAttributes) && extraAttributes.compareToIgnoreCase("memberOf") == 0) {
				ldapAttribute = extraAttributes;
			}

			if (StringUtils.isNotEmpty(username)) {

				List<UserDetails> userDetailsList = ControllerUtil.ldapSearch(ldapUrls, ldapUsername, ldapPassword,
						ldapUserSearchBase, ldapUserSearchFilter, ldapUserDisplayNameAttr, ldapUserMailAttr,
						ldapAttribute, username);

				if (userDetailsList != null && userDetailsList.size() > 0) {
					userDetails = userDetailsList.get(0);
				}
			}
		}

		return userDetails;
	}

	public UserDetails getUserDetails() throws LdapTemplateException {
		return getUserDetailsById(null, null);
	}

}
