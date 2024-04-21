package org.lox.vistor;

import org.lox.abstractsyntaxtree.*;

public class ReversePolishNotationVisitor implements ExpressionVisitor<String> {

  public String convertToReversePolishNotation(Expression expression) {
    return expression.accept(this);
  }

  @Override
  public String visitBinaryExpr(BinaryExpression expr) {
    final StringBuilder binaryExpression = new StringBuilder();

    binaryExpression.append(expr.getLeftHandExpression().accept(this));
    binaryExpression.append(" ");
    binaryExpression.append(expr.getRightHandExpression().accept(this));
    binaryExpression.append(" ");
    binaryExpression.append(expr.getOperator().lexeme());

    return binaryExpression.toString();
  }

  @Override
  public String visitGroupingExpr(GroupingExpression expr) {
    return null;
  }

  @Override
  public String visitLiteralExpr(LiteralExpression expr) {
    return expr.getValue().toString();
  }

  @Override
  public String visitUnaryExpr(UnaryExpression expr) {
    return null;
  }
}
