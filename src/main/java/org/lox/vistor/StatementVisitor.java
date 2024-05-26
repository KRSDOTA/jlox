package org.lox.vistor;

import org.lox.abstractsyntaxtree.statement.BlockStatement;
import org.lox.abstractsyntaxtree.statement.ExpressionStatement;
import org.lox.abstractsyntaxtree.statement.PrintStatement;
import org.lox.abstractsyntaxtree.statement.VariableStatement;

public interface StatementVisitor<T> {

    T visitExpressionStatement(ExpressionStatement expressionStatement);
    T visitPrintStatement(PrintStatement printStatement);
    T visitVariableStatement(VariableStatement variableStatement);
    T visitBlockStatement(BlockStatement blockStatement);
}
