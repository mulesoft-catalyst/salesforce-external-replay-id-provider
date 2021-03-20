/*
 * (c) 2003-2020 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.internal.keyfactories;

import org.mule.encryption.key.SymmetricKeyFactory;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * Creates a key for decryption
 * </p>
 *
 * @author MuleSoft, Inc.
 */
public class SymmetricEncryptionKeyFactory implements SymmetricKeyFactory {

  private String algorithm;
  private byte[] key;

  public SymmetricEncryptionKeyFactory(String algorithm, String key) {
    validateKey(key);
    this.algorithm = algorithm;
    this.key = key.getBytes();
  }

  @Override
  public Key encryptionKey() {
    return new SecretKeySpec(key, algorithm);
  }

  private void validateKey(String key) {
    if (key == null) {
      throw new IllegalArgumentException("If keystore is not defined then the key is considered to be " +
          "an encryption key in Base64 encoding");
    }
  }
}
