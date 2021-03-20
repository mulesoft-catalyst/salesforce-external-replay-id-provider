package com.mulesoft.external.provider.common;

public interface Constants {
	
	  // TODO replace with you extension name. This must be a meaningful name for this module.
    public static final String EXTENSION_NAME = "Salesforce Replayid Property Provider";//Custom Properties Provider
    public static final String CONFIG_ELEMENT = "config";
    
    public static final String SALESFORCE_REPLAYID_CLIENT_PARAMETER_GROUP = "Salesforce Replayid";
    
	
//	public static final String PARAMETER_CUSTOM_PARAMETER = "customParameter";
	public static final String PARAMETER_END_POINT_URL = "providerEndPointURL";
	public static final String PARAMETER_CLIENT_ID = "clientId";
	public static final String PARAMETER_CLIENT_SECRET = "clientSecret";
	public static final String PARAMETER_IS_EXTERNAL_PROVIDER = "isExternalProvider";
	public static final String PARAMETER_EVENT_TYPE = "eventType";
	public static final String PARAMETER_CONTINUE_ON_ERROR = "continueOnError";
	public static final String PARAMETER_CONFIG_FILE_NAME = "configFileName";
//	public static final String PARAMETER_ENV_VARIABLE = "envVariableName";

	public static final String HTTP_METHOD_GET = "GET";
	
	
	public static final String HEADER_CLIENT_ID = "client_id";
	public static final String HEADER_CLIENT_SECRET = "client_secret";
	
	
	
	
	public final static String CUSTOM_PROPERTIES_PREFIX = "custom-sfdc-property::";
	public static final String KEY_SFDC_REPLY_ID = "last_replay_id";
	
	public static final String VENDOR_NAME = "Extended Loader";
	
	public static final String SUFFIX_FWD_SLASH = "/";
	
	public static final String FILE_EXTENSION_YAML = ".yaml";
	
	public static final String KEY_REPLAY_ID = "replayid";
	
	
	
	//{url=https://anypoint.mulesoft.com/mocking/api/v1/links/59ae3fb0-60b1-4702-a1ed-48c938be7b9f/replayId/, clientId=testId, clientSecret=testSecret, eventType=appt_test, continueOnError=false}
	
	public static final Object KEY_URL = "url";
	public static final Object KEY_CLIENT_ID ="x-client-id";
	public static final Object KEY_CLIENT_SECRET = "x-client-secret";
	public static final Object KEY_EVENT_TYPE = "x-event-type";
	public static final Object KEY_CONTINUE_ON_ERROR ="x-continue-on-error";
	public static final Object KEY_REPLAY_ID_PROVIDER = "x-replay-id-provider";
	

//	public static final Object KEY_URL = "url";
//	public static final Object KEY_CLIENT_ID ="clientId";
//	public static final Object KEY_CLIENT_SECRET = "clientSecret";
//	public static final Object KEY_EVENT_TYPE = "eventType";
//	public static final Object KEY_CONTINUE_ON_ERROR ="continueOnError";
//	public static final Object KEY_REPLAY_ID_PROVIDER = "replayIdProvider";

	

}
