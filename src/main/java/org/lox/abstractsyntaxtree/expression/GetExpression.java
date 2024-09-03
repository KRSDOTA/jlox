package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class GetExpression extends Expression {

    private final Token name;
    private final Expression object;

    public GetExpression(Token name, Expression object) {
        this.name = name;
        this.object = object;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitGetExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getObject() {
        return object;
    }
}
