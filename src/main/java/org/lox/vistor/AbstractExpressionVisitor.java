package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.*;
import org.lox.typecomparison.DoubleAndStringComparison;
import org.lox.typecomparison.StringAndDoubleAddition;
import org.lox.typecomparison.StringAndDoubleComparison;
import org.lox.typecomparison.StringAndStringComparison;

import static org.lox.typecomparison.ValueOperations.*;

public abstract class AbstractExpressionVisitor implements ExpressionVisitor<Object> {

    private final DoubleAndStringComparison doubleAndStringComparison = new DoubleAndStringComparison();
    private final StringAndDoubleComparison stringAndDoubleComparison = new StringAndDoubleComparison();
    private final StringAndStringComparison stringAndStringComparison = new StringAndStringComparison();
    private final StringAndDoubleAddition stringAndDoubleAddition = new StringAndDoubleAddition();

    @Override
    public Object visitBinaryExpr(BinaryExpression expr) {
        Object evaluatedLeftExpression = evaluate(expr.getLeftHandExpression());
        Object evaluatedRightExpression = evaluate(expr.getRightHandExpression());

        switch (expr.getOperator().tokenType()){
            case MINUS -> {
                checkNumberOperand(expr.getOperator(), evaluatedRightExpression);
                return (double) evaluatedLeftExpression - (double) evaluatedRightExpression; }
            case SLASH -> {
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression /  (double) evaluatedRightExpression; }
            case STAR -> {
                checkNumberOperand(expr.getOperator(), evaluatedRightExpression);
                return (double) evaluatedLeftExpression *  (double) evaluatedRightExpression; }
            case PLUS -> {
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return (String) evaluatedLeftExpression + (String) evaluatedRightExpression;
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleAddition.addStringAndDouble((String) evaluatedRightExpression, (Double) evaluatedLeftExpression);
                }
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleAddition.addStringAndDouble((String) evaluatedLeftExpression, (Double) evaluatedRightExpression);
                }
                if(evaluatedLeftExpression instanceof Double && evaluatedRightExpression instanceof  Double) {
                    return (double) evaluatedLeftExpression + (double) evaluatedRightExpression;
                }
                throw new RuntimeError(expr.getOperator(), "Operands must be two strings");
            }
            case GREATER -> {
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleComparison.greater((String) evaluatedLeftExpression, (Double) evaluatedRightExpression);
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return doubleAndStringComparison.greater((Double) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndStringComparison.greater((String) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression > (double) evaluatedRightExpression; }
            case GREATER_EQUAL -> {
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleComparison.greaterOrEqual((String) evaluatedLeftExpression, (Double) evaluatedRightExpression);
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return doubleAndStringComparison.greaterOrEqual((Double) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndStringComparison.greaterOrEqual((String) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression >= (double) evaluatedRightExpression;
            }
            case LESS -> {
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleComparison.less((String) evaluatedLeftExpression, (Double) evaluatedRightExpression);
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return doubleAndStringComparison.less((Double) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndStringComparison.less((String) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression < (double) evaluatedRightExpression;
            }
            case LESS_EQUAL -> {
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndDoubleComparison.lessOrEqual((String) evaluatedLeftExpression, (Double) evaluatedRightExpression);
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return doubleAndStringComparison.lessOrEqual((Double) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return stringAndStringComparison.lessOrEqual((String) evaluatedLeftExpression, (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression <= (double) evaluatedRightExpression;
            }
            case BANG_EQUAL -> {return !isEqual(evaluatedLeftExpression, evaluatedRightExpression); }
            case EQUAL_EQUAL -> {return isEqual(evaluatedLeftExpression, evaluatedRightExpression); }
            case COMMA -> { return evaluatedRightExpression; } // discard left expression without evaluating?
            default -> throw new Error("Opps");
        }
    }

    protected Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitGroupingExpr(GroupingExpression expr) {
        return evaluate(expr.getGroupedExpression());
    }

    @Override
    public Object visitLiteralExpr(LiteralExpression expr) {
        return expr.getValue();
    }

    @Override
    public Object visitUnaryExpr(UnaryExpression expr) {
        Object right = evaluate(expr);

        switch(expr.getOperator().tokenType()){
            case MINUS  -> { return -(double) right; }
            case BANG  -> { return !isTruthy(right); }
        }

        return null;
    }

    @Override
    public Object visitConditionalExpr(ConditionalExpression expr) {
        Object conditional = evaluate(expr.getExpression());
        if (isTruthy(conditional)) {
            return evaluate(expr.getThenBranch());
        }

        return evaluate(expr.getElseBranch());
    }

    @Override
    public Object visitLogicalExpression(LogicalExpression logicalExpression){
        Object left = evaluate(logicalExpression.getLeft());
        Object right = evaluate(logicalExpression.getRight());

        switch (logicalExpression.getOperator().tokenType()){
            case OR: {
                if (isTruthy(left)){
                    return true;
                }
                return isTruthy(right);
            }
            case AND:
            {
                return isTruthy(left) && isTruthy(right);
            }
            default: {
                throw new RuntimeError(logicalExpression.getOperator(), "unsupported operation");
            }
        }
    }

}
