package org.example.scanning;

public record Token(TokenType tokenType, String lexeme, Object literal, int line) {

  public String toString() {
    return tokenType + " " + lexeme + " " + literal;
  }
}
