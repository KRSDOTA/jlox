package org.lox.abstractsyntaxtree;

import org.lox.vistor.StatementVisitor;

public class ExpressionStatement extends Statement {
    private final Expression statement;

    public ExpressionStatement(Expression statement) {
        this.statement = statement;
    }

    <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitExpressionStatement(this);
    }

    public Expression getStatement() {
        return statement;
    }
}
