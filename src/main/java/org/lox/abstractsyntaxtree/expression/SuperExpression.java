package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class SuperExpression extends Expression {
    private final Token keyword;
    private final Token method;

    public SuperExpression(Token keyword, Token method) {
        this.keyword = keyword;
        this.method = method;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitSuperExpression(this);
    }

    public Token getMethod() {
        return method;
    }

    public Token getKeyword() {
        return keyword;
    }
}
