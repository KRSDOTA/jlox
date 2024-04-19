package org.lox.abstractsyntaxtree;

import org.lox.vistor.Visitor;

public class LiteralExpression extends Expression {

  final Object value;

  public LiteralExpression(Object value) {
    this.value = value;
  }

  @Override
  <T> T accept(Visitor<T> visitor) {
    return visitor.visitLiteralExpr(this);
  }
}
