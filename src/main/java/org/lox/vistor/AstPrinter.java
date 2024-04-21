package org.lox.vistor;

import org.lox.abstractsyntaxtree.*;

public class AstPrinter implements ExpressionVisitor<String> {

  public String printTree(Expression expression) {
    return expression.accept(this);
  }
  @Override
  public String visitBinaryExpr(BinaryExpression expr) {
    return parenthesize(expr.getOperator().lexeme(),
      expr.getLeftHandExpression(), expr.getRightHandExpression());
  }

  @Override
  public String visitGroupingExpr(GroupingExpression expr) {
    return parenthesize("group", expr.getGroupedExpression());
  }

  @Override
  public String visitLiteralExpr(LiteralExpression expr) {
    if (expr.getValue() == null) return "nil";
    return expr.getValue().toString();
  }

  @Override
  public String visitUnaryExpr(UnaryExpression expr) {
    return parenthesize(expr.getOperator().lexeme(), expr.getRightHandExpression());
  }

  private String parenthesize(String name, Expression... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);
    for (Expression expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");

    return builder.toString();
  }
}
