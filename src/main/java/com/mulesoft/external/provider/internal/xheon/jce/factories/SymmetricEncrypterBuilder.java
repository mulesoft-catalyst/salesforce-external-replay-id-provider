package com.mulesoft.external.provider.internal.xheon.jce.factories;

import static com.mulesoft.external.provider.internal.xheon.jce.EncryptionPadding.PKCS5Padding;

import org.mule.encryption.Encrypter;
import org.mule.encryption.jce.JCEEncrypter;

import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncrypterBuilder;
import com.mulesoft.external.provider.internal.xheon.jce.encryption.EncryptionAlgorithm;
import com.mulesoft.external.provider.internal.xheon.keyfactories.SymmetricEncryptionKeyFactory;

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
