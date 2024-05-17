package org.lox.abstractsyntaxtree.expression;

import org.lox.vistor.ExpressionVisitor;

public abstract class Expression {

  public abstract <T> T accept(ExpressionVisitor<T> expressionVisitor);
}
