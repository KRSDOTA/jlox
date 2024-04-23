package org.lox.parser;

import org.lox.abstractsyntaxtree.BinaryExpression;
import org.lox.abstractsyntaxtree.Expression;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;
import java.util.List;

import static org.lox.scanning.TokenType.*;

public class Parser {
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  private Expression expression() {
    Expression expression = comparison();

    while(match(BANG_EQUAL, EQUAL_EQUAL)) {
      final Token operator = previous();
      final Expression right = comparison();
      expression = new BinaryExpression(expression, operator, right);
    }

    return expression;
  }

  private Expression comparison() {
    Expression expression = term();

    while(true){

    }
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
     if(check(type)) {
       advance();
       return true;
     }
    }
    return false;
  }

  private boolean check(TokenType type) {
    if(isAtEnd()) {
      return false;
    }
    return peek().tokenType() == type;
  }
  private Token advance() {
    if(!isAtEnd()) {
      current++;
    }
    return previous();
  }

  private boolean isAtEnd() {
    return peek().tokenType() == EOF;
  }

  private Token peek() {
   return tokens.get(current);
  }

 private Token previous() {
   return tokens.get(current - 1);
 }

}
