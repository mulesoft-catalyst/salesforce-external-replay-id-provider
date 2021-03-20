/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.api;

import static org.mule.runtime.api.component.ComponentIdentifier.builder;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.inject.Inject;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.config.api.dsl.model.ConfigurationParameters;
import org.mule.runtime.config.api.dsl.model.ResourceProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProviderFactory;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.mulesoft.external.provider.common.Constants;


public class ExternalConfigurationPropertiesProviderFactory implements ConfigurationPropertiesProviderFactory {


	public final Logger logger = LoggerFactory.getLogger(ExternalConfigurationPropertiesProviderFactory.class);
	
	
	public static final String EXTENSION_NAMESPACE = Constants.EXTENSION_NAME.toLowerCase().replace(" ", "-"); //Salesforce Replayid Property Provider
	public static final String SALESFORCE_REPLAYID_CLIENT__PARAMETER_GROUP_NAME = Constants.SALESFORCE_REPLAYID_CLIENT_PARAMETER_GROUP.toLowerCase().replace(" ", "-");
	private static final ComponentIdentifier CUSTOM_PROPERTIES_PROVIDER = builder().namespace(EXTENSION_NAMESPACE)
			.name(Constants.CONFIG_ELEMENT).build();
	
	
	@Inject
	public ExpressionManager expressionManager;
	
	int count = 0;

	@Override
	public ComponentIdentifier getSupportedComponentIdentifier() {
		
		return CUSTOM_PROPERTIES_PROVIDER;
	}

	@Override
	public ConfigurationPropertiesProvider createProvider(ConfigurationParameters parameters,
			ResourceProvider externalResourceProvider) {
		
		
		
		logger.debug("Entering ExternalConfigurationPropertiesProviderFactory.createProvider >>");
				
		String configFileName = parameters.getStringParameter(Constants.PARAMETER_CONFIG_FILE_NAME);
		
		String endPointURL = "";
		String eventType = "";
		String clientId = "";
		String clientSecret = "";
		boolean continueOnError = false;
		Yaml configYaml = null;

		try {
			
			configYaml = new Yaml();
			
			InputStream inputStream = this.getClass()
					  .getClassLoader()
					  .getResourceAsStream(configFileName);
					Map<String, Object> yamlMap = configYaml.load(inputStream);
					
			Map<String, Object> replayIdProviderMap = (Map<String, Object>) yamlMap.get(Constants.KEY_REPLAY_ID_PROVIDER);
			
			endPointURL = (String) replayIdProviderMap.get(Constants.KEY_URL);
			eventType = (String) replayIdProviderMap.get(Constants.KEY_EVENT_TYPE);
			clientId = (String) replayIdProviderMap.get(Constants.KEY_CLIENT_ID);
			clientSecret = (String) replayIdProviderMap.get(Constants.KEY_CLIENT_SECRET);
			
			continueOnError = Boolean.parseBoolean((String) replayIdProviderMap.get(Constants.KEY_CONTINUE_ON_ERROR));
			

		} catch (Exception e) {
			logger.error("Unable to retrieve config for config file =" + configFileName +" . Error Message =" + e.getMessage());
		}

		String externalStoreEndPointURL ="";
		
		if(endPointURL.endsWith(Constants.SUFFIX_FWD_SLASH)) {
			String encodedEventType = eventType;
//			try {
//				encodedEventType = java.net.URLEncoder.encode(eventType,"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.out.println("Error During encoding");
//			}
			externalStoreEndPointURL = endPointURL+ encodedEventType;
		} else {
			externalStoreEndPointURL = endPointURL.trim() + Constants.SUFFIX_FWD_SLASH+ eventType.trim();
		}
		
		System.out.println("externalStoreEndPointURL::" + externalStoreEndPointURL);

		ExternalConfigurationPropertiesProvider externalConfigurationPropertiesProvider = new ExternalConfigurationPropertiesProvider(externalStoreEndPointURL, clientId, clientSecret, continueOnError);
		logger.debug("<< Exiting ExternalConfigurationPropertiesProviderFactory.createProvider.");
		return externalConfigurationPropertiesProvider;
		
	}

}
