package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class VariableExpression extends Expression {
  public final Token token;

  public VariableExpression(Token token) {
    this.token = token;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitVariableExpr(this);
  }

  public Token getToken() {
    return token;
  }
}
