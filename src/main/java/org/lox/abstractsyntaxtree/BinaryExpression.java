package org.lox.abstractsyntaxtree;

import org.lox.scanning.Token;
import org.lox.vistor.Visitor;

public class BinaryExpression extends Expression {

  private final Expression leftHandExpression;

  private final Token operator;

  private final Expression rightHandExpression;

  public BinaryExpression(Expression leftHandExpression, Token operator, Expression rightHandExpression) {
    this.leftHandExpression = leftHandExpression;
    this.operator = operator;
    this.rightHandExpression = rightHandExpression;
  }

  @Override
  <T> T accept(Visitor<T> visitor) {
    return visitor.visitBinaryExpr(this);
  }
}
