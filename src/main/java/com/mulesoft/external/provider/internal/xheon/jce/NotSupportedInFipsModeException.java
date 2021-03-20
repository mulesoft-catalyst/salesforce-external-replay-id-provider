package com.mulesoft.external.provider.internal.xheon.jce;

public class NotSupportedInFipsModeException extends Exception {

  public NotSupportedInFipsModeException(String s) {
    super(s);
  }

  public NotSupportedInFipsModeException(String s, Exception e) {
    super(s, e);
  }
}
