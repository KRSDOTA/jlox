package org.lox.errorhandler;

public interface JLoxErrorHandler {
  void reportError(int line, String message);

  boolean hadError();

  public void resetError();
}
