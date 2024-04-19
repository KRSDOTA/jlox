package org.lox.abstractsyntaxtree;

import org.lox.vistor.ExpressionVisitor;

public class LiteralExpression extends Expression {

  final Object value;

  public LiteralExpression(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitLiteralExpr(this);
  }
}
