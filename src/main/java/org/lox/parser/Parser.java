package org.lox.parser;

import org.lox.abstractsyntaxtree.*;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;

import java.util.List;

import static org.lox.scanning.TokenType.*;

public class Parser {

  private final static TokenType[] EQUALITY_OPERATORS = new TokenType[]{BANG_EQUAL, EQUAL_EQUAL};
  private final static TokenType[] INEQUALITY_OPERATORS = new TokenType[]{GREATER, GREATER_EQUAL, LESS, LESS_EQUAL};

  private final List<Token> tokens;
  private int current = 0;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public Expression expression() {
    Expression expression = comparison();

    while (matchUnconsumedToken(EQUALITY_OPERATORS)) {
      final Token operator = consumeToken();
      final Expression right = comparison();
      expression = new BinaryExpression(expression, operator, right);
    }

    return expression;
  }

  private Expression comparison() {
    Expression expression = term();

    while (matchUnconsumedToken(INEQUALITY_OPERATORS)) {
      final Token operator = consumeToken();
      final Expression right = term();
      expression = new BinaryExpression(expression, operator, right);
    }

    return expression;
  }

  private Expression term() {
    Expression expression = factor();

    while (matchUnconsumedToken(MINUS, PLUS)) {
      final Token operator = consumeToken();
      final Expression right = factor();
      expression = new BinaryExpression(expression, operator, right);
    }

    return expression;
  }

  private Expression factor() {
    Expression expression = unary();

    while (matchUnconsumedToken(SLASH, STAR)) {
      final Token operator = consumeToken();
      final Expression right = unary();
      expression = new BinaryExpression(expression, operator, right);
    }

    return expression;
  }

  private Expression unary() {
    if (matchUnconsumedToken(BANG, MINUS)) {
      final Token operator = consumeToken();
      final Expression right = unary();
      return new UnaryExpression(operator, right);
    }

    return primary();
  }

  private Expression primary() {
    if (matchUnconsumedToken(FALSE)) {
      return new LiteralExpression(false);
    }

    if (matchUnconsumedToken(TRUE)) {
      return new LiteralExpression(true);
    }

    if (matchUnconsumedToken(NIL)) {
      return new LiteralExpression(null);
    }

    if (matchUnconsumedToken(NUMBER, STRING)) {
      return new LiteralExpression(consumeToken().literal());
    }

    if(matchUnconsumedToken(LEFT_PAREN)) {
      Expression expression = expression();
//      consume(RIGHT_PAREN, "Expect ')' after expression.");
      return new GroupingExpression(expression);
    }

    throw new Error("Lol");
  }


  private boolean matchUnconsumedToken(TokenType... types) {
    for (TokenType type : types) {
      if (doesNextTokenMatch(type)) {
        return true;
      }
    }
    return false;
  }

  private boolean doesNextTokenMatch(TokenType type) {
    if (isAtEndOfTokenStream()) {
      return false;
    }
    return tokens.get(current).tokenType() == type;
  }

  private Token consumeToken() {
    if (!isAtEndOfTokenStream()) {
      current++;
    }
    return mostRecentlyConsumedToken();
  }

  private boolean isAtEndOfTokenStream() {
    return current > tokens.size() - 1 || tokens.get(current).tokenType() == EOF;
  }

  private Token mostRecentlyConsumedToken() {
    return tokens.get(current - 1);
  }

}
