package org.lox.vistor;

import org.lox.abstractsyntaxtree.BinaryExpression;
import org.lox.abstractsyntaxtree.GroupingExpression;
import org.lox.abstractsyntaxtree.LiteralExpression;
import org.lox.abstractsyntaxtree.UnaryExpression;

public interface ExpressionVisitor<T> {
  T visitBinaryExpr(BinaryExpression expr);
  T visitGroupingExpr(GroupingExpression expr);
  T visitLiteralExpr(LiteralExpression expr);
  T visitUnaryExpr(UnaryExpression expr);
}

