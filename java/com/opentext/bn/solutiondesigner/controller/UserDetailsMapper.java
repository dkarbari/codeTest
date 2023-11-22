package com.opentext.bn.solutiondesigner.controller;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.opentext.bn.solutiondesigner.vo.UserDetails;

public class UserDetailsMapper implements AttributesMapper<UserDetails> {
	private String ldapUserDisplayNameAttr = "";
	private String ldapUserMailAttr = "";
	private String ldapAttr = "";

	public UserDetailsMapper(String ldapUserDisplayNameAttr, String ldapUserMailAttr, String ldapAttr) {
		this.ldapUserDisplayNameAttr = ldapUserDisplayNameAttr;
		this.ldapUserMailAttr = ldapUserMailAttr;
		this.ldapAttr = ldapAttr;
	}

	public UserDetails mapFromAttributes(Attributes attrs) throws NamingException {
		UserDetails userDetails = new UserDetails();
		userDetails.setUserDisplayNameAttr(ldapUserDisplayNameAttr);
		userDetails.setUserMailAttr(ldapUserMailAttr);

		if (!ldapUserDisplayNameAttr.isEmpty() && attrs.get(ldapUserDisplayNameAttr) != null) {
			String temp = attrs.get(ldapUserDisplayNameAttr).toString();
			userDetails.getAttrMap().put(ldapUserDisplayNameAttr,
					attrs.get(ldapUserDisplayNameAttr).toString().substring(temp.indexOf(":") + 1).trim());
		}

		if (!ldapUserMailAttr.isEmpty() && attrs.get(ldapUserMailAttr) != null) {
			String temp = attrs.get(ldapUserMailAttr).toString();
			userDetails.getAttrMap().put(ldapUserMailAttr,
					attrs.get(ldapUserMailAttr).toString().substring(temp.indexOf(":") + 1).trim());
		}

		if (!ldapAttr.isEmpty() && attrs.get(ldapAttr) != null) {
			String temp = attrs.get(ldapAttr).toString();
			userDetails.getAttrMap().put(ldapAttr,
					attrs.get(ldapAttr).toString().substring(temp.indexOf(":") + 1).trim());
		}

		return userDetails;
	}
}
