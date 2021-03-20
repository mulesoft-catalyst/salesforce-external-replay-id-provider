package com.mulesoft.external.provider.internal.xheon.jce.factories;

import static com.mulesoft.external.provider.internal.xheon.jce.EncryptionPadding.PKCS1PADDING;
import static com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm.RSA;
import static com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionMode.ECB;

import org.mule.encryption.Encrypter;
import org.mule.encryption.jce.JCEEncrypter;

import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncrypterBuilder;
import com.mulesoft.external.provider.internal.xheon.keyfactories.AsymmetricEncryptionKeyFactory;

public class AsymmetricEncrypterBuilder extends EncrypterBuilder {

  @Override
  public Encrypter build() {
    return new JCEEncrypter(RSA.name() + "/" + ECB.name() + "/" + PKCS1PADDING.name(), null,
                            new AsymmetricEncryptionKeyFactory(RSA.name(), key), false);
  }
}
