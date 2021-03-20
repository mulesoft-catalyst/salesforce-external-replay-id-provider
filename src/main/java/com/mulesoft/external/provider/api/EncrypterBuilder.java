/*
 * (c) 2003-2020 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.api;

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
