package com.opentext.bn.solutiondesigner;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.gxs.dare.common.utils.orch.OrchVaultUtils;
import com.gxs.orch.app.logging.SystemOutLogger;

import bn.ot.vault.replacers.common.rest.client.VaultException;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SolutionDesignerServicesApplication {
	
	private static final Logger logger = LogManager.getLogger(SolutionDesignerServicesApplication.class);
	
	public static void main(String[] args) {

		try {
			OrchVaultUtils.getInstance().initSecretsFromVault();
		} catch (VaultException e) {
			logger.error("Exception getting secrets from Vault: " + e.getMessage() + " will continue with defaults.", e);
		}

		SpringApplication.run(SolutionDesignerServicesApplication.class, args);
	}

	@PostConstruct
	public static void postConstruct() {
		SystemOutLogger.redirect();
	}

}
