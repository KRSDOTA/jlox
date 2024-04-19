package org.lox.abstractsyntaxtree;

import org.lox.vistor.ExpressionVisitor;

public class GroupingExpression extends Expression {
  final Expression groupedExpression;

  public GroupingExpression(Expression groupedExpression) {
    this.groupedExpression = groupedExpression;
  }

  public Expression getGroupedExpression() {
    return groupedExpression;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
    return expressionVisitor.visitGroupingExpr(this);
  }
}
