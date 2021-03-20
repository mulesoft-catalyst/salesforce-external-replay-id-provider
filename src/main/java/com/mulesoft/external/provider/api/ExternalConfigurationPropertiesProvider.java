package com.mulesoft.external.provider.api;

import java.io.BufferedReader;

//import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.mule.runtime.core.api.context.MuleContextAware;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.external.provider.common.Constants;

import shapeless.TypeOf;

public class ExternalConfigurationPropertiesProvider implements ConfigurationPropertiesProvider {

	private String providerEndpointUrl = "";
	private String clientId = "";
	private String clientSecret = "";
	private boolean continueOnError = false;
	private int count = 0;

	public final Logger logger = LoggerFactory.getLogger(ExternalConfigurationPropertiesProvider.class);



	public ExternalConfigurationPropertiesProvider(String endPointURL, String clientId, String clientSecret, boolean continueOnError) {
		this.providerEndpointUrl = endPointURL;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.continueOnError = continueOnError;

	}

	@Override
	public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
		boolean lookupError = false; // If Key not found then throw exception
		
		logger.debug("Entering >> getConfigurationProperty Key = " + configurationAttributeKey);
		Optional<ConfigurationProperty> configProperty = Optional.empty();

		
		if (configurationAttributeKey.startsWith(Constants.CUSTOM_PROPERTIES_PREFIX)) {
			String effectiveKey = configurationAttributeKey.substring(Constants.CUSTOM_PROPERTIES_PREFIX.length());

			if (effectiveKey.equals(Constants.KEY_SFDC_REPLY_ID)) {
				String replyID = retrieveDataFromSalesforce(providerEndpointUrl, clientId, clientSecret,
						configurationAttributeKey, count++);
				
				logger.info("Replay ID from Config Property Provider ="+replyID);
				
				if(!continueOnError && (replyID == null || replyID.trim().length() ==0)) {
					logger.error("Unable to retrieve ReplyID for "+ configurationAttributeKey + " from the property store: " + providerEndpointUrl + ". Please check your configuration and try again.");
					configProperty = null;
				} else {
					configProperty = Optional.of(new ConfigurationProperty() {

						@Override
						public Object getSource() {
							return "Custom Provider Source";
						}
						
						// Below method get invoked 2 times for each property look, potentially it could be a bug
						@Override
						public Object getRawValue() {
							return replyID;
						}

						@Override
						public String getKey() {
							return Constants.KEY_SFDC_REPLY_ID;
						}
					});
				}
				
			} // End of effective Key check if Block
		}// end of configurationAttributeKey if block
		
		logger.debug("Exiting >> retrieveDataFromRedis = " + configProperty);

		return configProperty;
	}

	private String retrieveDataFromSalesforce(String endPointURL, String clientId, String clientSecret,
			String configurationAttributeKey, int count)  {
		
		logger.debug("Entering>> retrieveDataFromRedis");
		
		
		
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = null;

		String replayId = "";
		try {

			
			URL redisEndPointUrl = new URL(endPointURL);
			connection = (HttpURLConnection) redisEndPointUrl.openConnection();
			connection.setRequestMethod(Constants.HTTP_METHOD_GET);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			connection.setRequestProperty(Constants.HEADER_CLIENT_ID, clientId);
			connection.setRequestProperty(Constants.HEADER_CLIENT_SECRET, clientSecret);
			
			int responseCode = connection.getResponseCode();
			logger.debug("Response Code::" + responseCode);
			
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String readLine = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = mapper.readValue(response.toString(), Map.class);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					
					
					if (entry.getValue() != null && (entry.getValue() instanceof  LinkedHashMap ) ) {
						LinkedHashMap<String, Object> lhm = (LinkedHashMap) entry.getValue();
						if (lhm.get(Constants.KEY_REPLAY_ID) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID);
							replayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID)).intValue());
//							logger.debug();
							break;
						}
					}

				}

			}

		} catch (MalformedURLException e) {
			logger.error("Error occured during key lookup using custom property provider" + e.getMessage());
		} catch (IOException e) {
			logger.error("Error occured during key lookup using custom property provider" + e.getMessage());
		} catch (Exception e) {
			logger.error("Error occured during key lookup using custom property provider" + e.getMessage());
		} finally {
			connection.disconnect();
		}
		
		logger.debug("Exiting >> retrieveDataFromRedis with Reply ID =" + replayId );
		return replayId;

	}

	@Override
	public String getDescription() {
		return "External Property Provider.";
	}

}
