/*
 * (c) 2003-2020 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.external.provider.internal.jce.factories;

import static com.mulesoft.external.provider.internal.jce.EncryptionPadding.PKCS5Padding;

import org.mule.encryption.Encrypter;
import org.mule.encryption.jce.JCEEncrypter;

import com.mulesoft.external.provider.api.EncrypterBuilder;
import com.mulesoft.external.provider.api.EncryptionAlgorithm;
import com.mulesoft.external.provider.internal.keyfactories.SymmetricEncryptionKeyFactory;

public class SymmetricEncrypterBuilder extends EncrypterBuilder {

  private EncryptionAlgorithm encryptionAlgorithm;

  public SymmetricEncrypterBuilder(EncryptionAlgorithm encryptionAlgorithm) {
    this.encryptionAlgorithm = encryptionAlgorithm;
  }

  @Override
  public Encrypter build() {
    return new JCEEncrypter(encryptionAlgorithm.name() + "/" + mode.name() + "/" + PKCS5Padding.name(), null,
                            new SymmetricEncryptionKeyFactory(encryptionAlgorithm.name(), key), useRandomIVs);
  }

}
