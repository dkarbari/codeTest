package com.opentext.bn.solutiondesigner.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public class UserDetails {
	private String userDisplayNameAttr;
	private String userMailAttr;
	private Map<String, String> attrMap;
    private  Set<GrantedAuthority> authorities;
	public String getUserDisplayNameAttr() {
		return userDisplayNameAttr;
	}

	public void setUserDisplayNameAttr(String userDisplayNameAttr) {
		this.userDisplayNameAttr = userDisplayNameAttr;
	}

	public String getUserMailAttr() {
		return userMailAttr;
	}

	public void setUserMailAttr(String userMailAttr) {
		this.userMailAttr = userMailAttr;
	}

	public Map<String, String> getAttrMap() {
		if (attrMap == null) {
			attrMap = new HashMap<>();
		}
		return attrMap;
	}

	public void setAttrMap(Map<String, String> attrMap) {
		this.attrMap = attrMap;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities=authorities;
		
	}

	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
}
