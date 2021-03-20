package com.mulesoft.external.provider.api;

import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.ALGORITHM_PARAMETER;
//import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.ENCRYPT_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.ENCRYPT_PARAMETER_TAG;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.FILE_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.KEY_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.MODE_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.USE_RANDOM_IV_PARAMETER;
import static com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm.AES;
import static com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionMode.CBC;
import static java.lang.String.format;
import static org.mule.encryption.jce.JCE.isJCEInstalled;
import static org.mule.runtime.api.component.ComponentIdentifier.builder;
import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.config.api.dsl.model.ConfigurationParameters;
import org.mule.runtime.config.api.dsl.model.ResourceProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProviderFactory;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.mulesoft.external.provider.common.Constants;
import com.mulesoft.external.provider.internal.xheon.jce.XheonSecureModule;
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm;
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionMode;


public class ExternalConfigurationPropertiesProviderFactory implements ConfigurationPropertiesProviderFactory {

	private static final String SHORT_KEY_MESSAGE = "You need to increment your key size."
			+ " The minimum allowed key size is: %d, but your key size is: %d";
	private static final String LONG_KEY_MESSAGE = "Your key size exceeds the maximum allowed key size in your JVM."
			+ " The maximum allowed key size is: %d, but your key size is: %d.";
	private static final String INSTALL_JCE_MESSAGE = "You need to install the Java Cryptography Extension (JCE) "
			+ "Unlimited Strength Jurisdiction Policy Files";

	public final Logger logger = LoggerFactory.getLogger(ExternalConfigurationPropertiesProviderFactory.class);

	public static final String EXTENSION_NAMESPACE = Constants.EXTENSION_NAME.toLowerCase().replace(" ", "-"); // Salesforce
																												// Replayid
																												// Property
																												// Provider
	public static final String SALESFORCE_REPLAYID_CLIENT__PARAMETER_GROUP_NAME = Constants.SALESFORCE_REPLAYID_CLIENT_PARAMETER_GROUP
			.toLowerCase().replace(" ", "-");
	private static final ComponentIdentifier CUSTOM_PROPERTIES_PROVIDER = builder().namespace(EXTENSION_NAMESPACE)
			.name(Constants.CONFIG_ELEMENT).build();

	@Inject
	public ExpressionManager expressionManager;

	int count = 0;


	
	@Override
	public ConfigurationPropertiesProvider createProvider(ConfigurationParameters parameters,
			ResourceProvider externalResourceProvider) {
		String file = parameters.getStringParameter(FILE_PARAMETER); 
		String key = parameters.getStringParameter(KEY_PARAMETER); 
		

		ComponentIdentifier encryptComponentIdentifier = ComponentIdentifier.builder().namespace(EXTENSION_NAMESPACE)
				.name(ENCRYPT_PARAMETER_TAG).build();
		
		
		EncryptionAlgorithm algorithm = getAlgorithm(
				parameters.getComplexConfigurationParameter(encryptComponentIdentifier));
		EncryptionMode mode = getMode(parameters.getComplexConfigurationParameter(encryptComponentIdentifier));

		validateKeyLength(key, algorithm);
	    
	    boolean useRandomIVs = Boolean.valueOf(parameters.getStringParameter(USE_RANDOM_IV_PARAMETER));
	    XheonSecureModule xheonr = new XheonSecureModule(algorithm, mode, key, useRandomIVs);
	    
	    String endPointURL = "";
		String eventType = "";
		String clientId = "";
		String clientSecret = "";
		boolean ignoreError = false;
		String defaultReplayId = "";
		Yaml configYaml = null;

		try {
			configYaml = new Yaml();
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
			Map<String, Object> yamlMap = configYaml.load(inputStream);

			Map<String, Object> replayIdMap = (Map<String, Object>) yamlMap
					.get(Constants.KEY_REPLAY_ID_PROVIDER);
			endPointURL = (String) replayIdMap.get(Constants.KEY_URL);
			eventType = (String) replayIdMap.get(Constants.KEY_EVENT_TYPE);
			clientId = (String) replayIdMap.get(Constants.KEY_CLIENT_ID); // encrypted
			clientSecret = (String) replayIdMap.get(Constants.KEY_CLIENT_SECRET); // encrypted
			defaultReplayId = (String) replayIdMap.get(Constants.DEFAULT_REPLAY_ID); 
			clientId = xheonr.convertPropertyValue(clientId);
			clientSecret = xheonr.convertPropertyValue(clientSecret);
//Decrypted
			ignoreError = Boolean.parseBoolean((String) replayIdMap.get(Constants.KEY_CONTINUE_ON_ERROR));

		} catch (Exception e) {
			logger.error("Unable to retrieve config for config file =" + file + " . Error Message ="
					+ e.getMessage());
		}

		String externalStoreEndPointURL = "";

		if (endPointURL.endsWith(Constants.SUFFIX_FWD_SLASH)) {
			String encodedEventType = eventType;
			externalStoreEndPointURL = endPointURL + encodedEventType;
		} else {
			externalStoreEndPointURL = endPointURL.trim() + Constants.SUFFIX_FWD_SLASH + eventType.trim();
		}

		ExternalConfigurationPropertiesProvider externalConfigurationPropertiesProvider = new ExternalConfigurationPropertiesProvider(
				externalStoreEndPointURL, key, clientSecret, defaultReplayId, ignoreError);
		logger.debug("<< Exiting ExternalConfigurationPropertiesProviderFactory.createProvider.");
		return externalConfigurationPropertiesProvider;

	}
	
	  private void validateKeyLength(String key, EncryptionAlgorithm algorithm) {
		    if (key.length() > algorithm.getMaxKeySize()) {
		      String message = format(LONG_KEY_MESSAGE, algorithm.getMaxKeySize(), key.length());
		      if (!isJCEInstalled()) {
		        message += INSTALL_JCE_MESSAGE;
		      }

		      throw new MuleRuntimeException(createStaticMessage(message));
		    } else if (key.length() < algorithm.getMinKeySize()) {
		      String message = format(SHORT_KEY_MESSAGE, algorithm.getMinKeySize(), key.length());
		      throw new MuleRuntimeException(createStaticMessage(message));
		    }
		  }

	private EncryptionAlgorithm getAlgorithm(List<ConfigurationParameters> encryptionParameters) {
		return EncryptionAlgorithm.valueOf(getProperty(encryptionParameters, ALGORITHM_PARAMETER, AES.name()));
	}

	private EncryptionMode getMode(List<ConfigurationParameters> encryptionParameters) {
		return EncryptionMode.valueOf(getProperty(encryptionParameters, MODE_PARAMETER, CBC.name()));
	}

	private String getProperty(List<ConfigurationParameters> encryptionParameters, String property,
			String defaultValue) {
		if (encryptionParameters.size() != 1) {
			return defaultValue;
		}

		String propertyValue = encryptionParameters.get(0).getStringParameter(property);
		return propertyValue != null ? propertyValue : defaultValue;
	}
  @Override
	public ComponentIdentifier getSupportedComponentIdentifier() {

		return CUSTOM_PROPERTIES_PROVIDER;
	}


}
