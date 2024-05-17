package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.*;

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
    return expr.accept(this);
  }

  @Override
  public String visitLiteralExpr(LiteralExpression expr) {
    return expr.getValue().toString();
  }

  @Override
  public String visitUnaryExpr(UnaryExpression expr) {
    final StringBuilder unaryExpression = new StringBuilder();
    unaryExpression.append(expr.getRightHandExpression().accept(this));
    unaryExpression.append(expr.getOperator().lexeme());
    return unaryExpression.toString();
  }

  @Override
  public String visitConditionalExpr(ConditionalExpression expr) {
    return "";
  }

  @Override
  public String visitVariableExpr(VariableExpression variableExpression) {
    return null;
  }
}
