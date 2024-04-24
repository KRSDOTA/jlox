package org.lox.vistor;

import org.lox.abstractsyntaxtree.*;

public interface ExpressionVisitor<T> {
  T visitBinaryExpr(BinaryExpression expr);
  T visitGroupingExpr(GroupingExpression expr);
  T visitLiteralExpr(LiteralExpression expr);
  T visitUnaryExpr(UnaryExpression expr);
  T visitConditionalExpr(ConditionalExpression expr);
}

