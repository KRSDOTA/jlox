package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.scanning.Token;

public class ThisExpression extends Expression {
    private final Token keyword;

    public ThisExpression(Token keyword) {
        this.keyword = keyword;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitThisExpression(this);
    }

    public Token getKeyword() {
        return keyword;
    }
}
