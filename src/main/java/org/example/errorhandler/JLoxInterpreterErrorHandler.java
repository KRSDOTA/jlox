package org.example.errorhandler;

public class JLoxInterpreterErrorHandler implements JLoxErrorHandler {

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

  private void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }
}
