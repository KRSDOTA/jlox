package org.lox.errorhandler;

import org.lox.scanning.Token;
import org.lox.scanning.TokenType;

public class JLoxParserErrorHandler implements JLoxErrorHandler {
  @Override
  public void reportError(int line, String message) {

  }

  @Override
  public void reportError(Token token, String message) {
   if(token.tokenType() == TokenType.EOF) {
     reportError(token.line(), "at end " + message);
   } else {
     reportError(token.line(), "at '" + token.lexeme() + "'" + message);
   }
  }

  @Override
  public boolean hadError() {
    return false;
  }

  @Override
  public void resetError() {

  }
}
