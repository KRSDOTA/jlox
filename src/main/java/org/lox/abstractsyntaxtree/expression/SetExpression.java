package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class SetExpression extends Expression {

    private final Expression object;

    private final Token name;

    private final Expression value;

    public SetExpression(Expression object, Token name, Expression value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitSetExpression(this);
    }

    public Expression getObject() {
        return object;
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}
