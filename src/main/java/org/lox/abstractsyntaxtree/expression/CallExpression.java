package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

import java.util.List;

public class CallExpression extends Expression {

    final Expression callee;
    final Token paren;
    final List<Expression> arguments;

   public CallExpression(Expression callee, Token paren, List<Expression> arguments) {
       this.callee = callee;
       this.paren = paren;
       this.arguments = arguments;
   }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visitCallExpression(this);
    }

    public Expression getCallee() {
        return callee;
    }

    public Token getParen() {
        return paren;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}
