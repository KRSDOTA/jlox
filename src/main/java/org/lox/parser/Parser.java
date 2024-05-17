package org.lox.parser;

import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.PrintStatement;
import org.lox.abstractsyntaxtree.statement.Statement;
import org.lox.abstractsyntaxtree.statement.VariableStatement;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxParserErrorHandler;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;

import java.util.ArrayList;
import java.util.List;

import static org.lox.scanning.TokenType.*;

public class Parser {

    private static class ParseError extends RuntimeException {
    }

    private final static TokenType[] EQUALITY_OPERATORS = new TokenType[]{BANG_EQUAL, EQUAL_EQUAL};
    private final static TokenType[] INEQUALITY_OPERATORS = new TokenType[]{GREATER, GREATER_EQUAL, LESS, LESS_EQUAL};

    private final JLoxErrorHandler jLoxErrorHandler = new JLoxParserErrorHandler();

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean hadError() {
        return jLoxErrorHandler.hadError();
    }

    public List<Statement> parse() {
     List<Statement> statements = new ArrayList<>();
     while (!isAtEndOfTokenStream()){
         statements.add(declaration());
     }
     return statements;
    }

  private Statement declaration() {
    try {
     if(matchUnconsumedToken(VAR)) {
       return varDeclaration();
     }
     return statement();
    } catch (ParseError error){
      synchronise();
      return null;
    }
  }

  private Statement varDeclaration() {
    Token name = consumeIfTokenMatchOtherwiseError(IDENTIFIER, "Expected a variable name");

    Expression initaliser = null;

    if(matchUnconsumedToken(EQUAL)){
      consumeToken();
      initaliser = expression();
    }

    consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ; after variable declaration");
    return new VariableStatement(initaliser, name);
  }

  private Statement statement() {
        if(matchUnconsumedToken(PRINT)){
            consumeToken();
            return printStatement();
        }
       return expressionStatement();
    }

    private Statement printStatement() {
        Expression value = expression();
        consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ';' after value");
        return new PrintStatement(value);
    }

    private Statement expressionStatement() {
        Expression value = expression();
        consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ';' after value");
        return new ExpressionStatement(value);
    }
    private Expression expression() {
        Expression leftBlockExpression = conditional();

        while (matchUnconsumedToken(COMMA)) {
            final Token operator = consumeToken();
            final Expression rightBlockExpression = conditional();
            leftBlockExpression = new BinaryExpression(leftBlockExpression, operator, rightBlockExpression);
        }

        return leftBlockExpression;
    }

    private Expression conditional() {
        Expression conditionalExpression = equality();
        if (matchUnconsumedToken(QUESTION)) {
            consumeToken();
            Expression thenBranch = expression();
            consumeIfTokenMatchOtherwiseError(TokenType.COLON, "Expected ':' after then branch of conditional expression");
            Expression elseBranch = conditional();
            conditionalExpression = new ConditionalExpression(conditionalExpression, thenBranch, elseBranch);
        }

        return conditionalExpression;
    }

    private Expression equality() {
        Expression leftAssociativeTree = comparison();

        while (matchUnconsumedToken(EQUALITY_OPERATORS)) {
            final Token operator = consumeToken();
            final Expression rightTree = comparison();
            leftAssociativeTree = new BinaryExpression(leftAssociativeTree, operator, rightTree);
        }

        return leftAssociativeTree;
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
        if (matchUnconsumedToken(BANG, MINUS, PLUS)) {
            final Token operator = consumeToken();
            final Expression right = unary();
            return new UnaryExpression(operator, right);
        }

        return primary();
    }

    private Expression primary() {
        if (matchUnconsumedToken(FALSE)) {
            consumeToken();
            return new LiteralExpression(false);
        }

        if (matchUnconsumedToken(TRUE)) {
            consumeToken();
            return new LiteralExpression(true);
        }

        if (matchUnconsumedToken(NIL)) {
            consumeToken();
            return new LiteralExpression(null);
        }

        if (matchUnconsumedToken(NUMBER, STRING)) {
            return new LiteralExpression(consumeToken().literal());
        }

        if (matchUnconsumedToken(LEFT_PAREN)) {
            consumeToken();
            Expression expression = expression();
            consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect ')' after expression.");
            return new GroupingExpression(expression);
        }

        if (matchUnconsumedToken(PLUS)) {
            consumeToken();
            error(mostRecentlyConsumedToken(), "Missing left hand operand for binary expression");
            term();
        }

        if (matchUnconsumedToken(BANG, BANG_EQUAL)) {
            consumeToken();
            error(mostRecentlyConsumedToken(), "Missing left-hand operand");
            return null;
        }

        if (matchUnconsumedToken(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            consumeToken();
            error(mostRecentlyConsumedToken(), "Expected an expression");
            comparison();
            return null;
       }

        throw error(tokens.get(current), "Expected an Expression");
    }

  private void synchronise() {
    consumeToken();

    while (!isAtEndOfTokenStream()) {
      if (mostRecentlyConsumedToken().tokenType() == SEMICOLON) {
        return;
      }
      switch (tokens.get(current).tokenType()) {
        case CLASS:
        case FUN:
        case VAR:
        case FOR:
        case IF:
        case WHILE:
        case PRINT:
        case RETURN:
          return;
      }

    }

    consumeToken();
  }

  private Token consumeIfTokenMatchOtherwiseError(TokenType tokenType, String message) {
    if (doesNextTokenMatch(tokenType)) {
      return consumeToken();
    }

    throw error(this.tokens.get(current), message);
  }

  private ParseError error(Token token, String message) {
    jLoxErrorHandler.reportError(token, message);
    return new ParseError();
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
