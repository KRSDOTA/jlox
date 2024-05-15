package org.lox.vistor;

import org.lox.abstractsyntaxtree.ExpressionStatement;
import org.lox.abstractsyntaxtree.PrintStatement;

public interface StatementVisitor<T> {

    T visitExpressionStatement(ExpressionStatement expressionStatement);

    T visitPrintStatement(PrintStatement printStatement);
}
