package org.lox.errorhandler;

import org.lox.scanning.Token;

public class JLoxLexerErrorHandler implements JLoxErrorHandler {

  private boolean hadError = false;

  public boolean hadError() {
    return hadError;
  }

  public void resetError() {
    this.hadError = false;
  }

  public void reportError(int line, String message) {
    report(line, "", message);
  }

  @Override
  public void reportError(Token token, String message) {

  }

  private void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }
}
