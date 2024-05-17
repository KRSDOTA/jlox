package org.lox.abstractsyntaxtree.expression;

import org.lox.vistor.ExpressionVisitor;

public class ConditionalExpression extends Expression {

    private Expression expression;

    private Expression thenBranch;

    private Expression elseBranch;

    public ConditionalExpression(Expression expression, Expression thenBranch, Expression elseBranch) {
        this.expression = expression;
        this.elseBranch = elseBranch;
        this.thenBranch = thenBranch;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
      return expressionVisitor.visitConditionalExpr(this);
    }

    public Expression getExpression() {
        return expression;
    }

    public Expression getThenBranch() {
        return thenBranch;
    }

    public void setThenBranch(Expression thenBranch) {
        this.thenBranch = thenBranch;
    }

    public Expression getElseBranch() {
        return elseBranch;
    }
}
