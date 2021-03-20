package com.mulesoft.external.provider.internal.xheon.jce.encryption;

public interface EncrypterBuilderFactory {

  EncrypterBuilder createFor(EncryptionAlgorithm algorithm);
}
