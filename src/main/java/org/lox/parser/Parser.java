package org.lox.parser;

import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.*;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxParserErrorHandler;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;

import javax.swing.plaf.nimbus.State;
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
    if(matchUnconsumedToken(FUN)){
        consumeToken();
        return function("function");
    }
     if(matchUnconsumedToken(VAR)) {
       consumeToken();
       return varDeclaration();
     }
     return statement();
    } catch (ParseError error){
      synchronise();
      return null;
    }
  }

    private Statement function(String kind) {
        Token name = consumeIfTokenMatchOtherwiseError(IDENTIFIER, "Expect " + kind + " name.");
        List<Token> parameters = new ArrayList<>();

        if(!doesNextTokenMatch(RIGHT_PAREN)){
           do {
              if(parameters.size() >= 255) {
                  error(tokens.get(current), "can;t have more than 255 parameters");
              }
              parameters.add(consumeIfTokenMatchOtherwiseError(IDENTIFIER, "Expect parameter name"));
           } while(matchUnconsumedToken(COMMA));
        }
        consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect ')' after parameters");
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
        if(matchUnconsumedToken(FOR)){
            consumeToken();
            return forStatement();
        }
        if(matchUnconsumedToken(PRINT)){
            consumeToken();
            return printStatement();
        }
        if(matchUnconsumedToken(WHILE)){
            consumeToken();
            return whileStatement();
        }
        if(matchUnconsumedToken(IF)){
            consumeToken();
            return ifStatement();
        }
        if(matchUnconsumedToken(LEFT_BRACE)){
            consumeToken();
            return new BlockStatement(block());
        }
       return expressionStatement();
    }

    private Statement forStatement() {
       consumeIfTokenMatchOtherwiseError(LEFT_PAREN, "Expect '(' after 'for' .");

       Statement initaliser;
       if(matchUnconsumedToken(SEMICOLON)){
           consumeToken();
           initaliser = null;
       }
       if(matchUnconsumedToken(VAR)){
           consumeToken();
           initaliser = varDeclaration();
       }
       else {
           initaliser = expressionStatement();
       }

       Expression condition = null;
       if(!doesNextTokenMatch(SEMICOLON)){
           condition = expression();
       }
       consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ';' after loop condition");

       Expression increment = null;
       if(!doesNextTokenMatch(RIGHT_PAREN)){
           increment = expression();
       }
       consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect ') after for clauses. ");

       Statement body = statement();
       if(increment != null){
           body = new BlockStatement(
            List.of(body, new ExpressionStatement(increment))
           );
       }

       if(condition == null){
           condition = new LiteralExpression(true);
       }
       body = new WhileStatement(condition, body);

       if(initaliser != null){
           body = new BlockStatement(List.of(initaliser, body));
       }

       return body;
    }

    private Statement printStatement() {
        Expression value = expression();
        consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ';' after value ");
        return new PrintStatement(value);
    }

    private Statement whileStatement() {
        consumeIfTokenMatchOtherwiseError(LEFT_PAREN, "Expect '(' after 'while' ");
        Expression whileCondition = expression();
        consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect ')' before body of 'while' ");
        Statement body = statement();
        return new WhileStatement(whileCondition, body);
    }

    private Statement ifStatement() {
       consumeIfTokenMatchOtherwiseError(LEFT_PAREN, "Expect '(' after 'if' ");
       Expression condition = expression();
       consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect closing ')' after expression ");
       Statement thenBranch = statement();
       Statement elseBranch = null;
       if(doesNextTokenMatch(ELSE)) {
           consumeToken();
           elseBranch = statement();
       }
       return new IfStatement(condition, thenBranch, elseBranch);
    }

    private List<Statement> block() {
      List<Statement> statements = new ArrayList<>();

      while (!doesNextTokenMatch(RIGHT_BRACE)){
          statements.add(declaration());
      }

      consumeIfTokenMatchOtherwiseError(RIGHT_BRACE, "Expect '}' after block. ");
      return statements;
    }

    private Statement expressionStatement() {
        Expression value = expression();
        consumeIfTokenMatchOtherwiseError(SEMICOLON, "Expect ';' after value ");
        return new ExpressionStatement(value);
    }
    private Expression expression() {
        return comma();
    }

    private Expression comma() {
        Expression leftBlockExpression = assignment();

        while (matchUnconsumedToken(COMMA)) {
            final Token operator = consumeToken();
            final Expression rightBlockExpression = assignment();
            leftBlockExpression = new BinaryExpression(leftBlockExpression, operator, rightBlockExpression);
        }

        return leftBlockExpression;
    }

    private Expression assignment() {
        Expression expression = conditional();

        if(matchUnconsumedToken(EQUAL)) {
            Token equals = consumeToken();
            Expression value = assignment();

            if(expression instanceof VariableExpression) {
                Token name = ((VariableExpression) expression).getToken();
                return new AssignmentExpression(value, name);
            }

            error(equals, "Invalid assignment target");
        }

        return expression;
    }

    private Expression conditional() {
        Expression conditionalExpression = or();
        if (matchUnconsumedToken(QUESTION)) {
            consumeToken();
            Expression thenBranch = expression();
            consumeIfTokenMatchOtherwiseError(TokenType.COLON, "Expected ':' after then branch of conditional expression");
            Expression elseBranch = conditional();
            conditionalExpression = new ConditionalExpression(conditionalExpression, thenBranch, elseBranch);
        }

        return conditionalExpression;
    }

    private Expression or(){
        Expression expression = and();

        while(matchUnconsumedToken(OR)){
            Token operator = consumeToken();
            Expression right = and();
            expression = new LogicalExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression and(){
        Expression expression = equality();

        while(matchUnconsumedToken(AND)){
            Token operator = consumeToken();
            Expression right = equality();
            expression = new LogicalExpression(expression, operator, right);
         }

        return expression;
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

        return call();
    }

    private Expression call(){
        Expression expression = primary();

        while(true){
            if(matchUnconsumedToken(LEFT_PAREN)){
                consumeToken();
                expression = finishCall(expression);
            } else {
                break;
            }
        }

        return expression;
    }

    private Expression finishCall(Expression callee){
        List<Expression> arguments = new ArrayList<>();
        if(!doesNextTokenMatch(RIGHT_PAREN)){
           arguments.add(expression());
          while (matchUnconsumedToken(COMMA)) {
              if(arguments.size() >= 255){
                  error(tokens.get(current), "What the hell are you doing having a function take more than 255 arguments");
              }
              consumeToken();
              arguments.add(expression());
          }
        }

        Token paren = consumeIfTokenMatchOtherwiseError(RIGHT_PAREN, "Expect ')' after argument");

        return new CallExpression(callee, paren, arguments);
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

        if(matchUnconsumedToken(IDENTIFIER)){
          return new VariableExpression(consumeToken());
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
