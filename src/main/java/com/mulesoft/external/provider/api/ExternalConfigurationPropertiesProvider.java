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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.external.provider.common.Constants;

public class ExternalConfigurationPropertiesProvider implements ConfigurationPropertiesProvider {

	private String providerEndpointUrl = "";
	private String clientId = "";
	private String clientSecret = "";
	private String defaultReplayId = "";
	private boolean ignoreError = false;
	private int count = 0;

	public final Logger logger = LoggerFactory.getLogger(ExternalConfigurationPropertiesProvider.class);

	public ExternalConfigurationPropertiesProvider(String endPointURL, String clientId, String clientSecret,
			String defaultReplayId,boolean ignoreError) {
		this.providerEndpointUrl = endPointURL;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.ignoreError = ignoreError;
		this.defaultReplayId = defaultReplayId;

	}

	@Override
	public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
		Optional<ConfigurationProperty> configProperty = Optional.empty();

		if (configurationAttributeKey.startsWith(Constants.CUSTOM_PROPERTIES_PREFIX)) {
			String effectiveKey = configurationAttributeKey.substring(Constants.CUSTOM_PROPERTIES_PREFIX.length());

			if (effectiveKey.equals(Constants.KEY_SFDC_REPLAY_ID)) {

				String lastReplayId = getLastReplayIdFromExternalSource(providerEndpointUrl, clientId, clientSecret,
						configurationAttributeKey, count++);
				logger.debug("Replay ID from External Source: [" + lastReplayId + "]");
				if (!ignoreError && (lastReplayId == null || lastReplayId.trim().length() == 0)) {
					logger.error(
							"External Source Configuration: Replay ID can't be loaded for ${" + configurationAttributeKey + "} from the property external source [ "
									+ providerEndpointUrl + "]. Please check your connection and try again.");
					logger.debug("External Source: No Replay ID provided setting to default_replay_id : [" + lastReplayId +"]");
					
					configProperty = Optional.of(new ConfigurationProperty() {

						@Override
						public Object getSource() {
							return "External Configuration Provider";
						}

						@Override
						public Object getRawValue() {
							if (defaultReplayId !=null && defaultReplayId.trim().length() != 0)
								return defaultReplayId;
							else
								return "-1";
						}

						@Override
						public String getKey() {
							return Constants.KEY_SFDC_REPLAY_ID;
						}
					});
				} else {
					logger.debug("External Source: Found Replay ID provided: [" + lastReplayId +"]");
					configProperty = Optional.of(new ConfigurationProperty() {

						@Override
						public Object getSource() {
							return "External Configuration Provider";
						}

						@Override
						public Object getRawValue() {
							return lastReplayId;
						}

						@Override
						public String getKey() {
							return Constants.KEY_SFDC_REPLAY_ID;
						}
					});
				}

			}
		}

		return configProperty;
	}

	private String getLastReplayIdFromExternalSource(String url, String clientId, String clientSecret,
			String configurationAttributeKey, int count) {
		
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = null;
		String lastReplayId = "";
		try {

			URL http = new URL(url);
			connection = (HttpURLConnection) http.openConnection();
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

					if (entry.getValue() != null && (entry.getValue() instanceof LinkedHashMap)) {
						LinkedHashMap<String, Object> lhm = (LinkedHashMap) entry.getValue();
						if (lhm.get(Constants.KEY_REPLAY_ID_1) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_1);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_1)).intValue());
							break;
						}else if (lhm.get(Constants.KEY_REPLAY_ID_2) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_2);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_2)).intValue());
							break;
						}else if (lhm.get(Constants.KEY_REPLAY_ID_3) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_3);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_3)).intValue());
							break;
						}else if (lhm.get(Constants.KEY_REPLAY_ID_4) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_4);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_4)).intValue());
							break;
						}else if (lhm.get(Constants.KEY_REPLAY_ID_5) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_5);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_5)).intValue());
							break;
						}else if (lhm.get(Constants.KEY_REPLAY_ID_6) != null) {
							Double d = (Double) lhm.get(Constants.KEY_REPLAY_ID_6);
							lastReplayId = String.valueOf(((Double) lhm.get(Constants.KEY_REPLAY_ID_6)).intValue());
							break;
						}

					}

				}

			}

		} catch (MalformedURLException e) {
			logger.error("HTTP_ERROR:" + e.getMessage());
		} catch (IOException e) {
			logger.error("HTTP_ERROR:" + e.getMessage());
		} catch (Exception e) {
			logger.error("HTTP_ERROR:" + e.getMessage());
		} finally {
			connection.disconnect();
		}
		return lastReplayId;

	}

	@Override
	public String getDescription() {
		return "External Property Provider.";
	}

}
