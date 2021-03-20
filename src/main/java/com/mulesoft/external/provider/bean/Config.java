package com.mulesoft.external.provider.bean;

public class Config {
	
	
	
	public Config() {
		super();
	}
	
	
	

	private ReplayIdProviderConfig replayIdProvider;
	
	

	public ReplayIdProviderConfig getReplayIdProvider() {
		return replayIdProvider;
	}

	public void setReplayIdProvider(ReplayIdProviderConfig replayIdProvider) {
		this.replayIdProvider = replayIdProvider;
	}

	
	
	
	@Override
	public String toString() {
		return "Config [replayIdProvider=" + replayIdProvider + "]";
	}

	

	
	
	
	

}
