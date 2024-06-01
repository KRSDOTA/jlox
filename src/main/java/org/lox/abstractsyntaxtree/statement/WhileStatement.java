package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.vistor.StatementVisitor;

public class WhileStatement extends Statement {

    private final Expression condition;

    private final Statement body;

    public WhileStatement(Expression condition, Statement body){
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitWhileStatement(this);
    }

    public Expression getCondition() {
        return condition;
    }

   public Statement getStatement() {
        return body;
   }
}
