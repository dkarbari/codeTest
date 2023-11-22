package com.opentext.bn.solutiondesigner.configuration;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.opentext.bn.solutiondesigner.controller.UserDetailsMapper;
import com.opentext.bn.solutiondesigner.vo.UserDetails;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

	@Value("${ldap.group.search.base}")
	private String ldapGroupSearchBase;

	@Value("${ldap.enabled}")
	private String ldapEnabled;

	@Value("${ldap.ignore.ssl.certificates}")
	private String ldapIgnoreSslCertificates;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		if (Boolean.parseBoolean(ldapIgnoreSslCertificates)) {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLContext.setDefault(ctx);
		}

		
	/*	http.authorizeRequests().antMatchers("/").fullyAuthenticated().and().formLogin().loginPage("/")
		.usernameParameter("userId").defaultSuccessUrl("/?designer=true", true).failureUrl("/?designer=false")
		.loginProcessingUrl("/perform_login").permitAll().and().csrf().disable().logout()
		.logoutUrl("/perform_logout").logoutSuccessUrl("/?logout=1").invalidateHttpSession(true)
		.deleteCookies("JSESSIONID").permitAll();*/
		
		http
	    .authorizeRequests()
	    .antMatchers("/")
	    .hasAnyRole("R_BN_SERVICE_DEV","R_BN_ITINERARY_DEV")
	    .antMatchers("/")
	    .fullyAuthenticated()
	    .and()
	    .formLogin()
	    .loginPage("/")
	    .usernameParameter("userId")
	    .defaultSuccessUrl("/?designer=true")
	    .failureUrl("/?designer=false")
	    .loginProcessingUrl("/perform_login")
	    .permitAll()
	    .and()
        .exceptionHandling()
        .accessDeniedHandler((request, response, exception) -> {
            response.sendRedirect("/?designer=false");
        })
	    .and()
	    .csrf().disable()
	    .logout()
	    .logoutUrl("/perform_logout")
	    .logoutSuccessUrl("/?logout=1")
	    .invalidateHttpSession(true)
	    .deleteCookies("JSESSIONID")
	    .permitAll();
	}
	

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.debug(true);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsMapper ldapUserDetailsMapper = new UserDetailsMapper(null, null, null);
		UserDetails ud=new UserDetails();
		if (Boolean.parseBoolean(ldapEnabled)) {
			auth.eraseCredentials(false).ldapAuthentication().contextSource().url(ldapUrls).managerDn(ldapUsername)
					.managerPassword(ldapPassword).and()

					// For an understanding of how this work
					// I used
					// https://docs.spring.io/spring-security/site/docs/3.0.x/reference/ldap.html,
					// section Loading Authorities
					// I also connected to LDAP server using JXplorer to understand

					.userSearchFilter(ldapUserSearchFilter).groupSearchBase(ldapGroupSearchBase)
					.userSearchBase(ldapUserSearchBase)
					
					;

		} else {
			auth.eraseCredentials(false).inMemoryAuthentication().withUser("admin").password("{noop}admin")
			.roles("R_BN_ITINERARY_DEV", "R_BN_SERVICE_DEV").and()
			.withUser("ITINERARY_DEVELOPER").password("{noop}abc123").roles("R_BN_ITINERARY_DEV").and()
			.withUser("SERVICE_DEVELOPER").password("{noop}abc123").roles("R_BN_SERVICE_DEV")
			;

		}
	}
	

	
	

}