package org.lox.abstractsyntaxtree;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

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
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitBinaryExpr(this);
  }

  public Expression getLeftHandExpression() {
    return leftHandExpression;
  }

  public Token getOperator() {
    return operator;
  }

  public Expression getRightHandExpression() {
    return rightHandExpression;
  }

}
