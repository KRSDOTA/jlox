package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class AssignmentExpression extends Expression{

  private final Expression value;
  private final Token token;

  public AssignmentExpression(Expression value, Token token) {
    this.value = value;
    this.token = token;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitAssignmentExpr(this);
  }

  public Expression getValue() {
    return value;
  }

  public Token getToken() {
    return token;
  }
}
