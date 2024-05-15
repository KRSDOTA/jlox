package org.lox.abstractsyntaxtree;

public class PrintStatement extends Statement {
    private final Expression statement;

    public PrintStatement(Expression statement) {
        this.statement = statement;
    }
}
