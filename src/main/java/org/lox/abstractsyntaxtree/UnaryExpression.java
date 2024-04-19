package org.lox.abstractsyntaxtree;

import org.lox.scanning.Token;
import org.lox.vistor.Visitor;

public class UnaryExpression extends Expression {
  private final Token operator;

  private final Expression rightHandExpression;

  public UnaryExpression(Token operator, Expression rightHandExpression) {
    this.operator = operator;
    this.rightHandExpression = rightHandExpression;
  }

  @Override
  <T> T accept(Visitor<T> visitor) {
    return visitor.visitUnaryExpr(this);
  }
}
