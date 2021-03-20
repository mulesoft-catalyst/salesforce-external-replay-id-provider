/*
 * (c) 2003-2020 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.internal.keyfactories;

import org.mule.encryption.key.EncryptionKeyFactory;
import org.mule.runtime.core.api.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * <p>
 * Creates a key for encryption
 * </p>
 *
 * @author MuleSoft, Inc.
 */
public class AsymmetricEncryptionKeyFactory implements EncryptionKeyFactory {

  private String algorithm;
  private byte[] key;

  public AsymmetricEncryptionKeyFactory(String algorithm, String key) {
    validateKey(key);
    this.algorithm = algorithm;
    this.key = Base64.decode(key);
  }

  @Override
  public Key encryptionKey() {
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      return kf.generatePrivate(spec);
    } catch (Exception e) {
      throw new RuntimeException("Could not build the Encryption key", e);
    }
  }

  @Override
  public Key decryptionKey() {
    X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      return kf.generatePublic(spec);
    } catch (Exception e) {
      throw new RuntimeException("Could not build the descryption key", e);
    }
  }

  private void validateKey(String key) {
    if (key == null) {
      throw new IllegalArgumentException("If keystore is not defined then the key is considered to be " +
          "an encryption key in Base64 encoding");
    }
  }

}
