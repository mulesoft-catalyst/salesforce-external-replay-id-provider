package com.mulesoft.external.provider.internal.xheon.keyfactories;

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
