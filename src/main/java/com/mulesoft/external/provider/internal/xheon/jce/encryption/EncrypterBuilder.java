package com.mulesoft.external.provider.internal.xheon.jce.encryption;

import org.mule.encryption.Encrypter;

public abstract class EncrypterBuilder {

  protected EncryptionMode mode;
  protected String key;
  protected boolean useRandomIVs;

  public EncrypterBuilder using(EncryptionMode mode) {
    this.mode = mode;
    return this;
  }

  public abstract Encrypter build();

  public EncrypterBuilder forKey(String key) {
    this.key = key;
    return this;
  }

  public EncrypterBuilder useRandomIVs(boolean useRandomIVs) {
    this.useRandomIVs = useRandomIVs;
    return this;
  }
}
