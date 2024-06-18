package org.lox.abstractsyntaxtree.expression;

import org.lox.abstractsyntaxtree.statement.Statement;
import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

import java.util.List;

public class FunctionExpression extends Expression {

    private final Token name;

    private final List<Token> params;

    private final List<Statement> body;

    public FunctionExpression(Token name, List<Token> params, List<Statement> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitFunctionExpression(this);
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Statement> getBody() {
        return body;
    }
}
