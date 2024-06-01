package org.lox.vistor;

import org.lox.abstractsyntaxtree.statement.*;

public interface StatementVisitor<T> {

    T visitExpressionStatement(ExpressionStatement expressionStatement);
    T visitPrintStatement(PrintStatement printStatement);
    T visitVariableStatement(VariableStatement variableStatement);
    T visitBlockStatement(BlockStatement blockStatement);
    T visitIfStatement(IfStatement ifStatement);
    T visitWhileStatement(WhileStatement whileStatement);
}
