package com.mulesoft.external.provider.internal.xheon.jce;

import org.mule.encryption.Encrypter;
import org.mule.encryption.exception.MuleEncryptionException;
import org.mule.encryption.exception.MuleInvalidAlgorithmConfigurationException;
import org.mule.runtime.core.api.util.Base64;

import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm;
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Module for binary encryption
 * </p>
 *
 * @author MuleSoft, Inc.
 */
public class XheonSecureModule {

  private static final String FIPS_SECURITY_MODEL = "fips140-2";
  private static final String PROPERTY_SECURITY_MODEL = "mule.security.model";
  private static final String FIPS_MODE_MESSAGE = "You're running in FIPS mode " +
      " so please verify that the algorithm is compliant with FIPS.";


  /**
   * <p>
   * Encrypter to decrypt properties values
   * </p>
   */
  private Encrypter encrypter;

  /**
   * <p>
   * The encryption algorithm used
   * </p>
   * <p/>
   * <p>
   * Allowed algorithms AES(Default), Blowfish, DES, DESede, RC2, RSA, PBEWithMD5AndDES
   * </p>
   */
  private EncryptionAlgorithm encryptionAlgorithm;

  /**
   * <p>
   * The encryption mode used
   * </p>
   * <p/>
   * <p>
   * Allowed modes CBC, CFB, ECB, OFB, PCBC
   * </p>
   */
  private EncryptionMode encryptionMode;

  /**
   * <p>
   * Use random initial vectors (IVs). In case of decryption, it assumes IV is prepended on the ciphertext
   * </p>
   */
  private boolean useRandomIVs;

  public XheonSecureModule(EncryptionAlgorithm algorithm, EncryptionMode mode, String key, boolean useRandomIVs) {
    encryptionAlgorithm = algorithm;
    encryptionMode = mode;
    this.useRandomIVs = useRandomIVs;
    buildEncrypter(key);
  }

  private void buildEncrypter(String key) {
    encrypter = encryptionAlgorithm.getBuilder().using(encryptionMode).forKey(readEnvironmentalProperties(key))
        .useRandomIVs(useRandomIVs).build();
  }

  private static boolean isFipsEnabled() {
    return FIPS_SECURITY_MODEL.equals(System.getProperty(PROPERTY_SECURITY_MODEL));
  }

  public byte[] decrypt(byte[] payload) throws MuleEncryptionException {
    try {
      return encrypter.decrypt(payload);
    } catch (MuleInvalidAlgorithmConfigurationException e) {
      if (isFipsEnabled()) {
        throw new MuleEncryptionException(e.getMessage(), new NotSupportedInFipsModeException(FIPS_MODE_MESSAGE, e));
      }

      throw e;
    }
  }

  public void setKey(String key) {
    buildEncrypter(key);
  }

  public boolean isEncrypted(String value) {
    return value.startsWith("![") && value.endsWith("]");
  }

  public String convertPropertyValue(String originalValue) {
    if (!isEncrypted(originalValue)) {
      return originalValue;
    }

    String propertyKey = originalValue.substring(2, originalValue.length() - 1);

    try {
      return new String(decrypt(Base64.decode(propertyKey)));
    } catch (MuleEncryptionException e) {
      throw new RuntimeException(e);
    }
  }

  public String readEnvironmentalProperties(String text) {
    Pattern propertyPatter = Pattern.compile("\\$\\{([^}]+)}");
    Matcher propertyMatcher = propertyPatter.matcher(text);
    String modifiedText = text;
    while (propertyMatcher.find()) {
      String property = propertyMatcher.group(1);
      modifiedText = replaceProperty(modifiedText, property);
    }

    return modifiedText;
  }

  private String replaceProperty(String modifiedText, String property) {
    String propertyValue = System.getProperty(property);
    checkForPropertyExistence(property, propertyValue);
    propertyValue = convertPropertyValue(propertyValue);
    String pattern = "\\$\\{(" + property + ")}";
    Pattern replacement = Pattern.compile(pattern);
    Matcher replacementMatcher = replacement.matcher(modifiedText);
    replacementMatcher.find();
    return replacementMatcher.replaceAll(Matcher.quoteReplacement(propertyValue));
  }

  private void checkForPropertyExistence(String property, String propertyValue) {
    if (propertyValue == null) {
      throw new RuntimeException("Property " + property + " could not be found");
    }
  }

}
