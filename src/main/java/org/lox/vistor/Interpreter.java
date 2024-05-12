package org.lox.vistor;

import org.lox.abstractsyntaxtree.BinaryExpression;
import org.lox.abstractsyntaxtree.ConditionalExpression;
import org.lox.abstractsyntaxtree.Expression;
import org.lox.abstractsyntaxtree.GroupingExpression;
import org.lox.abstractsyntaxtree.LiteralExpression;
import org.lox.abstractsyntaxtree.UnaryExpression;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.typecomparison.DoubleAndStringComparison;
import org.lox.typecomparison.StringAndDoubleComparison;

import static org.lox.typecomparison.ValueTest.*;

public class Interpreter implements ExpressionVisitor<Object> {
    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();

    private final DoubleAndStringComparison doubleAndStringComparison = new DoubleAndStringComparison();

    private final StringAndDoubleComparison stringAndDoubleComparison = new StringAndDoubleComparison();

    public void interpret(Expression expression){
        try {
            final Object evaluatedExpression = evaluate(expression);
            System.out.println(stringify(evaluatedExpression));
        } catch (RuntimeError error){
            errorHandler.reportError(error.token, error.getMessage());
        }
    }

    public boolean hasError() {
        return errorHandler.hadError();
    }

    private String stringify(Object evaluatedExpression) {
     if(evaluatedExpression == null) {
         return "nil";
     }
     if(evaluatedExpression instanceof Double){
         String text = evaluatedExpression.toString();
         if(text.endsWith(".0")){
             text = text.substring(0, text.length() - 2);
         }
         return text;
     }
     return evaluatedExpression.toString();
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

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
                if(evaluatedLeftExpression instanceof Double && evaluatedRightExpression instanceof  Double) {
                    return (double) evaluatedLeftExpression + (double) evaluatedRightExpression;
                }
                if (evaluatedLeftExpression instanceof String && evaluatedRightExpression instanceof String){
                    StringBuilder builder = new StringBuilder();
                    builder.append((String) evaluatedLeftExpression);
                    builder.append((String) evaluatedRightExpression);
                    return builder.toString();
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

                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);

                return (double) evaluatedLeftExpression > (double) evaluatedRightExpression; }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression >= (double) evaluatedRightExpression; }
            case LESS -> {
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression < (double) evaluatedRightExpression; }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression <= (double) evaluatedRightExpression; }
            case BANG_EQUAL -> {return !isEqual(evaluatedLeftExpression, evaluatedRightExpression); }
            case EQUAL_EQUAL -> {return isEqual(evaluatedLeftExpression, evaluatedRightExpression); }
            case COMMA -> { return evaluatedRightExpression; } // discard left expression without evaluating?
            default -> throw new Error("Opps");
        }
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

}
