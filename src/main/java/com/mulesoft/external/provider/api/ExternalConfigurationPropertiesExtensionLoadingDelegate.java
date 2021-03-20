package com.mulesoft.external.provider.api;

import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.ALGORITHM_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.ENCRYPT_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.FILE_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.KEY_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.MODE_PARAMETER;
import static com.mulesoft.external.provider.api.SecureConfigurationPropertiesConstants.USE_RANDOM_IV_PARAMETER;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.Category.SELECT;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.display.PathModel.Location.EMBEDDED;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
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
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm;
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionMode;

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
				.describedAs(String.format("Crafted %s Extension", Constants.EXTENSION_NAME)).withCategory(SELECT)
				.onVersion("1.0.0").fromVendor(Constants.VENDOR_NAME).withConfig(Constants.CONFIG_ELEMENT);

		ParameterGroupDeclarer defaultParameterGroup = configurationDeclarer.onDefaultParameterGroup();

		defaultParameterGroup.withRequiredParameter(FILE_PARAMETER)
				.ofType(BaseTypeBuilder.create(JAVA).stringType().build()).withExpressionSupport(NOT_SUPPORTED)
				.withDisplayModel(DisplayModel.builder()
						.path(new PathModel(FILE, false, EMBEDDED, new String[] { "yaml", "properties" })).build())
				.describedAs(" The location of the file with the secure configuration properties to use. "
						+ "It may be a location in the classpath or an absolute location. \nThe file location"
						+ " value may also contains references to properties that will only be resolved based on "
						+ "system properties or properties set at deployment time.");

		defaultParameterGroup.withRequiredParameter(KEY_PARAMETER)
				.ofType(BaseTypeBuilder.create(JAVA).stringType().build());

		ParameterGroupDeclarer parameterGroupDeclarer = configurationDeclarer.onParameterGroup(ENCRYPT_PARAMETER)
				.withDslInlineRepresentation(true);

		ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
		parameterGroupDeclarer.withOptionalParameter(ALGORITHM_PARAMETER)
				.ofType(typeLoader.load(EncryptionAlgorithm.class)).defaultingTo(EncryptionAlgorithm.AES);
		parameterGroupDeclarer.withOptionalParameter(MODE_PARAMETER).ofType(typeLoader.load(EncryptionMode.class))
				.defaultingTo(EncryptionMode.CBC);

		
		parameterGroupDeclarer.withOptionalParameter(USE_RANDOM_IV_PARAMETER)
				.ofType(BaseTypeBuilder.create(JAVA).booleanType().build())
				.withDisplayModel(DisplayModel.builder().displayName("Use random IVs").summary(
						"Use random initial vectors (IVs). In case of decryption, it assumes IV is prepended on the ciphertext.")
						.build())
				.withExpressionSupport(NOT_SUPPORTED).defaultingTo(Boolean.FALSE);
	}

}
