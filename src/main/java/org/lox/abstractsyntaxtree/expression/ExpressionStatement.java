package org.lox.abstractsyntaxtree.expression;

import org.lox.abstractsyntaxtree.statement.Statement;
import org.lox.vistor.StatementVisitor;

public class ExpressionStatement extends Statement {
    private final Expression statement;

    public ExpressionStatement(Expression statement) {
        this.statement = statement;
    }

    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitExpressionStatement(this);
    }

    public Expression getStatement() {
        return statement;
    }
}
