/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.api;

import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.FILE_PARAMETER;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.Category.SELECT;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.display.PathModel.Location.EMBEDDED;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclarer;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionLoadingDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.external.provider.common.Constants;

/**
 * Declares extension for Secure Properties Configuration module
 *
 * @since 1.0
 */
public class ExternalConfigurationPropertiesExtensionLoadingDelegate implements ExtensionLoadingDelegate {

	
	public final Logger logger = LoggerFactory.getLogger(ExternalConfigurationPropertiesExtensionLoadingDelegate.class);

	@Override
	public void accept(ExtensionDeclarer extensionDeclarer, ExtensionLoadingContext context) {

		logger.debug("Entering ExternalConfigurationPropertiesExtensionLoadingDelegate.accept >>");
		ConfigurationDeclarer configurationDeclarer = extensionDeclarer.named(Constants.EXTENSION_NAME)
				.describedAs(String.format("Crafted %s Extension", Constants.EXTENSION_NAME))
				.withCategory(SELECT)
				.onVersion("1.0.0")
				.fromVendor(Constants.VENDOR_NAME)
				.withConfig(Constants.CONFIG_ELEMENT);
		
		

		addSalesforceReplayIdParameters(configurationDeclarer);
		

	}

	private void addSalesforceReplayIdParameters(ConfigurationDeclarer configurationDeclarer) {
		
		ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
		
//		ParameterGroupDeclarer defaultParameterGroup = configurationDeclarer
//                .onParameterGroup(Constants.SALESFORCE_REPLAYID_CLIENT_PARAMETER_GROUP)
//                .withDslInlineRepresentation(true);
		
		ParameterGroupDeclarer defaultParameterGroup = configurationDeclarer.onDefaultParameterGroup();

		defaultParameterGroup.withRequiredParameter(Constants.PARAMETER_CONFIG_FILE_NAME)
        		.withDisplayModel(DisplayModel.builder().displayName("Provider Config File Name(including path)").build())
				.ofType(BaseTypeBuilder.create(JAVA).stringType().build())
				.withExpressionSupport(ExpressionSupport.SUPPORTED)
				.describedAs("Provider Config File Name(including path)");
		

	    defaultParameterGroup
        .withRequiredParameter(FILE_PARAMETER).ofType(BaseTypeBuilder.create(JAVA).stringType().build())
        .withExpressionSupport(NOT_SUPPORTED)
        .withDisplayModel(DisplayModel.builder().path(new PathModel(FILE, false, EMBEDDED, new String[] {"yaml", "properties"}))
            .build())
        .describedAs(" The location of the file with the secure configuration properties to use. "
            + "It may be a location in the classpath or an absolute location. \nThe file location"
            + " value may also contains references to properties that will only be resolved based on "
            + "system properties or properties set at deployment time.");

		
//		defaultParameterGroup.withRequiredParameter(Constants.PARAMETER_STORE_NAME)
//				.withDisplayModel(DisplayModel.builder().displayName("Event Store Name").build())
//				.ofType(BaseTypeBuilder.create(JAVA).stringType().build()).withExpressionSupport(SUPPORTED)
//				.describedAs("Event Store Name");
//
//		defaultParameterGroup.withRequiredParameter(Constants.PARAMETER_CLIENT_ID)
//				.withDisplayModel(DisplayModel.builder().displayName("Client ID").build())
//				.ofType(BaseTypeBuilder.create(JAVA).stringType().build())
//				.withExpressionSupport(SUPPORTED)
//				.describedAs("Client ID");
//
//		defaultParameterGroup.withRequiredParameter(Constants.PARAMETER_CLIENT_SECRET)
//				.withDisplayModel(DisplayModel.builder().displayName("Client Secret").build())
//				.ofType(BaseTypeBuilder.create(JAVA).stringType().build())
//				.withExpressionSupport(SUPPORTED)
//				.describedAs("Client Secret");

//    defaultParameterGroup
//	    .withRequiredParameter(Constants.PARAMETER_IS_EXTERNAL_PROVIDER).ofType(BaseTypeBuilder.create(JAVA).booleanType().build())
//	    .withExpressionSupport(NOT_SUPPORTED)
//	    .describedAs("Should we get property from External provider or from property file");

//		defaultParameterGroup.withRequiredParameter(Constants.PARAMETER_CONTINUE_ON_ERROR)
//				.ofType(BaseTypeBuilder.create(JAVA).booleanType().defaultValue("False").build())
//				.withExpressionSupport(NOT_SUPPORTED)
//				.describedAs("Application will not fail even if it is unable to retrieve replyId if set to true.");
	}

}
