package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.vistor.StatementVisitor;

public class PrintStatement extends Statement {
    private final Expression statement;

    public PrintStatement(Expression statement) {
        this.statement = statement;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitPrintStatement(this);
    }

    public Expression getStatement() {
        return statement;
    }
}
