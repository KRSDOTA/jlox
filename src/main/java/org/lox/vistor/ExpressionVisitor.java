package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.*;

public interface ExpressionVisitor<T> {
  T visitBinaryExpr(BinaryExpression binaryExpression);

  T visitGroupingExpr(GroupingExpression groupingExpression);

  T visitLiteralExpr(LiteralExpression literalExpression);

  T visitUnaryExpr(UnaryExpression unaryExpression);

  T visitConditionalExpr(ConditionalExpression conditionalExpression);

  T visitVariableExpr(VariableExpression variableExpression);

  T visitAssignmentExpr(AssignmentExpression assignmentExpression);

  T visitLogicalExpression(LogicalExpression logicalExpression);
}
