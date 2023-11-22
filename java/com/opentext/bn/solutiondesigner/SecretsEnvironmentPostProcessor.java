package com.opentext.bn.solutiondesigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * This class allows you to override the property source of your choosing with values taken from the secret property file.
 */
public class SecretsEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SecretsEnvironmentPostProcessor.class);

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		logger.warn("secret properties override was remove");
		return;
	}
}
