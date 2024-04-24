package org.lox.errorhandler;

import org.lox.scanning.Token;

public interface JLoxErrorHandler {
  void reportError(int line, String message);

  void reportError(Token token, String message);

  boolean hadError();

  public void resetError();
}
