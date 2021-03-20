/*
 * (c) 2003-2020 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.internal.jce.factories;

import static com.mulesoft.external.provider.api.EncryptionAlgorithm.RSA;
import static com.mulesoft.external.provider.api.EncryptionMode.ECB;
import static com.mulesoft.external.provider.internal.jce.EncryptionPadding.PKCS1PADDING;

import org.mule.encryption.Encrypter;
import org.mule.encryption.jce.JCEEncrypter;

import com.mulesoft.external.provider.api.EncrypterBuilder;
import com.mulesoft.external.provider.internal.keyfactories.AsymmetricEncryptionKeyFactory;

public class AsymmetricEncrypterBuilder extends EncrypterBuilder {

  @Override
  public Encrypter build() {
    return new JCEEncrypter(RSA.name() + "/" + ECB.name() + "/" + PKCS1PADDING.name(), null,
                            new AsymmetricEncryptionKeyFactory(RSA.name(), key), false);
  }
}
