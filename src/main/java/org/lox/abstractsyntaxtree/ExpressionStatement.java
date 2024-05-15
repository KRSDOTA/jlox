package org.lox.abstractsyntaxtree;

public class ExpressionStatement extends Statement {
    private final Expression statement;

    public ExpressionStatement(Expression statement) {
        this.statement = statement;
    }

}
