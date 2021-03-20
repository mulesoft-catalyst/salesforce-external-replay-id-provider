package com.mulesoft.external.provider.bean;

public class ReplayIdProviderConfig {
	
	
	public ReplayIdProviderConfig() {
		
	}
	
	private String url;
	private String clientId;
	private String clientSecret;
	private String eventType;
	private String continueOnError;
	
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	
	
	public String getContinueOnError() {
		return continueOnError;
	}
	public void setContinueOnError(String continueOnError) {
		this.continueOnError = continueOnError;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	
	@Override
	public String toString() {
		return "ReplayIdProviderConfig [url=" + url + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", eventType=" + eventType + ", continueOnError=" + continueOnError + "]";
	}
	
	
	
	

}
