package org.lox.abstractsyntaxtree;

import org.lox.vistor.Visitor;

public class GroupingExpression extends Expression {
  final Expression groupedExpression;

  public GroupingExpression(Expression groupedExpression) {
    this.groupedExpression = groupedExpression;
  }

  @Override
  <T> T accept(Visitor<T> visitor) {
    return visitor.visitGroupingExpr(this);
  }
}
