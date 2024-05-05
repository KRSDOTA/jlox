package org.lox.vistor;

import org.lox.abstractsyntaxtree.*;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.scanning.Token;

public class Interpreter implements ExpressionVisitor<Object> {
    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();

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
        Object leftExpression = evaluate(expr.getLeftHandExpression());
        Object rightExpression = evaluate(expr.getRightHandExpression());

        switch (expr.getOperator().tokenType()){
            case MINUS -> {
                checkNumberOperand(expr.getOperator(), rightExpression);
                return (double) leftExpression - (double) rightExpression; }
            case SLASH -> {
                checkNumberOperands(expr.getOperator(), leftExpression, rightExpression);
                return (double) leftExpression /  (double) rightExpression; }
            case STAR -> {
                checkNumberOperand(expr.getOperator(), rightExpression);
                return (double) leftExpression *  (double) rightExpression; }
            case PLUS -> {
                if(leftExpression instanceof Double && rightExpression instanceof  Double) {
                    return (double) leftExpression + (double) rightExpression;
                }
                if (leftExpression instanceof String && rightExpression instanceof String){
                    StringBuilder builder = new StringBuilder();
                    builder.append((String) leftExpression);
                    builder.append((String) rightExpression);
                    return builder.toString();
                }
                throw new RuntimeError(expr.getOperator(), "Operands must be two strings");
            }
            case GREATER -> {
                checkNumberOperands(expr.getOperator(), leftExpression, rightExpression);
                return (double) leftExpression > (double) rightExpression; }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.getOperator(), leftExpression, rightExpression);
                return (double) leftExpression >= (double) rightExpression; }
            case LESS -> {
                checkNumberOperands(expr.getOperator(), leftExpression, rightExpression);
                return (double) leftExpression < (double) rightExpression; }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.getOperator(), leftExpression, rightExpression);
                return (double) leftExpression <= (double) rightExpression; }
            case BANG_EQUAL -> {return !isEqual(leftExpression, rightExpression); }
            case EQUAL_EQUAL -> {return isEqual(leftExpression, rightExpression); }
            case COMMA -> { return rightExpression; } // discard left expression without evaluating?
            default -> throw new Error("Opps");
        }
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if(!(operand instanceof Double)){
            throw new RuntimeError(operator, "Operand must be a number");
        }
    }

    private void checkNumberOperands(Token operator, Object... operands) {
        for(int i = 0; i < operands.length; i++){
            checkNumberOperand(operator, operands[i]);
        }
    }

    private boolean isEqual(Object leftExpression, Object rightExpression) {
       if(leftExpression == null && rightExpression == null) {
           return true;
       }
       if(leftExpression == null){
           return false;
       }
       return leftExpression.equals(rightExpression);
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

    private boolean isTruthy(Object value) {
     if(value == null) {
         return false;
     } if(value instanceof Boolean){
          return (Boolean) value;
     }
     return true;
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
