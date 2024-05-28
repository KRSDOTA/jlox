package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.vistor.StatementVisitor;

public class IfStatement extends Statement {

    private final Expression condition;
    private final Statement thenBranch;
    private final Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitIfStatement(this);
    }

    public Statement getElseBranch() {
        return elseBranch;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    public Expression getCondition() {
        return condition;
    }
}
