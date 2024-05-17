package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class UnaryExpression extends Expression {
  private final Token operator;

  private final Expression rightHandExpression;

  public UnaryExpression(Token operator, Expression rightHandExpression) {
    this.operator = operator;
    this.rightHandExpression = rightHandExpression;
  }

  public Token getOperator() {
    return operator;
  }

  public Expression getRightHandExpression() {
    return rightHandExpression;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitUnaryExpr(this);
  }

}
