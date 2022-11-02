package com.mulesoft.external.provider.common;

public interface Constants {
	
	  // TODO replace with you extension name. This must be a meaningful name for this module.
    public static final String EXTENSION_NAME = "Salesforce Last Replay ID";//Custom Properties Provider
    public static final String CONFIG_ELEMENT = "config";
    
    public static final String SALESFORCE_REPLAYID_CLIENT_PARAMETER_GROUP = "Salesforce Last Replay ID";
    
	public static final String HTTP_METHOD_GET = "GET";
	
	
	public static final String HEADER_CLIENT_ID = "[CLIENT ID]";
	public static final String HEADER_CLIENT_SECRET = "[CLIENT SECRET]";
	
	
	
	
	public final static String CUSTOM_PROPERTIES_PREFIX = "custom-sfdc-property::";
	public static final String KEY_SFDC_REPLAY_ID = "last_replay_id";
	
	public static final String VENDOR_NAME = "Extended Loader";
	
	public static final String SUFFIX_FWD_SLASH = "/";
	
	public static final String FILE_EXTENSION_YAML = ".yaml";
	
	public static final String KEY_REPLAY_ID_1 = "last-replay-id";
	public static final String KEY_REPLAY_ID_2 = "replay-id";
	public static final String KEY_REPLAY_ID_3 = "replayId";
	public static final String KEY_REPLAY_ID_4 = "lastReplayId";
	public static final String KEY_REPLAY_ID_5 = "REPLAY_ID";
	public static final String KEY_REPLAY_ID_6 = "LAST_REPLAY_ID";

	
	
		
	public static final Object KEY_URL = "url";
	public static final Object KEY_CLIENT_ID ="[CLIENT ID]";
	public static final Object KEY_CLIENT_SECRET = "[CLIENT SECRET]";
	public static final Object KEY_EVENT_TYPE = "platform_event_name";
	public static final Object DEFAULT_REPLAY_ID ="default_replay_id";
	public static final Object KEY_CONTINUE_ON_ERROR ="ignore_error";
	public static final Object KEY_REPLAY_ID_PROVIDER = "sfdc_replay_provider";

	

}
