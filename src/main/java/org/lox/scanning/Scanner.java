package org.lox.scanning;

import org.lox.errorhandler.JLoxErrorHandler;

import java.util.ArrayList;
import java.util.List;

import static org.lox.scanning.TokenType.*;

public class Scanner {

  private final String source;
  private final JLoxErrorHandler jLoxErrorHandler;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  public Scanner(String source, JLoxErrorHandler jLoxErrorHandler) {
    this.source = source;
    this.jLoxErrorHandler = jLoxErrorHandler;
  }

  public List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(' -> addToken(LEFT_PAREN);
      case ')' -> addToken(RIGHT_PAREN);
      case '{' -> addToken(LEFT_BRACE);
      case '}' -> addToken(RIGHT_BRACE);
      case ',' -> addToken(COMMA);
      case '.' -> addToken(DOT);
      case '-' -> addToken(MINUS);
      case '+' -> addToken(PLUS);
      case ';' -> addToken(SEMICOLON);
      case '*' -> addToken(STAR);
      case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
      case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
      case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
      case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
      case '/' -> {
        if (match('/')) {
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(SLASH);
        }
      }
      case ' ', 'r', '\t' -> {
      }
      case '\n' -> line++;
      case '"' -> string();
      default -> {
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          jLoxErrorHandler.reportError(line, String.format("Unexpected character %s passed", c));
        }
      }
    }
  }

  private boolean match(char expected) {
    if (isAtEnd()) {
      return false;
    }
    if (source.charAt(current) != expected) {
      return false;
    }

    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd()) {
      return '\0'; // Null value in ASCII!
    }
    return source.charAt(current);
  }

  private void number() {
    while (isDigit(peek())) advance();

    if (peek() == '.' && isDigit(peekNext())) {
      advance();
      while (isDigit(peek())) advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  private char peekNext() {
    if (current + 1 >= source.length()) {
      return '\0';
    }
    return source.charAt(current + 1);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType tokenType) {
    addToken(tokenType, null);
  }

  private void addToken(TokenType tokenType, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(tokenType, text, literal, line));
  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }
    if (isAtEnd()) {
      jLoxErrorHandler.reportError(line, "Unterminated String");
      return;
    }

    advance();

    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    final String text = source.substring(start, current);
    TokenType type = ReservedWords.keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

}
