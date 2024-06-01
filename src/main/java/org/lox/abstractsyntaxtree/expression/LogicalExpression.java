package org.lox.abstractsyntaxtree.expression;

import org.lox.scanning.Token;
import org.lox.vistor.ExpressionVisitor;

public class LogicalExpression extends Expression {
    private final Expression left;
    private final Token operator;
    private final Expression right;

    public LogicalExpression(Expression left, Token operator, Expression right){
     this.left = left;
     this.operator = operator;
     this.right = right;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return null;
    }

    public Expression getLeft(){
       return left;
    }

    public Token getOperator(){
        return operator;
    }

    public Expression getRight(){
        return right;
    }
}
