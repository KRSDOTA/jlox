package org.lox.typecomparison;

import org.lox.scanning.Token;
import org.lox.vistor.RuntimeError;

import java.util.Optional;

public class ValueOperations {

    public static boolean isOperandsDoubleAndString(Object evaluatedLeftExpression, Object evaluatedRightExpression) {
        return evaluatedLeftExpression instanceof Double && evaluatedRightExpression instanceof String;
    }

    public static boolean isOperandsStringAndDouble(Object evaluatedLeftExpression, Object evaluatedRightExpression) {
        return evaluatedLeftExpression instanceof String && evaluatedRightExpression instanceof Double;
    }

    public static boolean isOperandsStringAndString(Object evaluatedLeftExpression, Object evaluatedRightExpression) {
        return evaluatedLeftExpression instanceof String && evaluatedRightExpression instanceof String;
    }
    public static void checkNumberOperand(Token operator, Object operand) {
        if(!(operand instanceof Double)){
            throw new RuntimeError(operator, "Operand must be a number");
        }
    }

    public static Optional<Double> tryParseDouble(String string) {
        try {
            return Optional.of(Double.parseDouble(string));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

   public static void checkNumberOperands(Token operator, Object... operands) {
        for(int i = 0; i < operands.length; i++){
            checkNumberOperand(operator, operands[i]);
        }
    }

    public static boolean isEqual(Object leftExpression, Object rightExpression) {
        if(leftExpression == null && rightExpression == null) {
            return true;
        }
        if(leftExpression == null){
            return false;
        }
        return leftExpression.equals(rightExpression);
    }

    public static boolean isTruthy(Object value) {
        if(value == null) {
            return false;
        } if(value instanceof Boolean){
            return (Boolean) value;
        }
        return true;
    }

   public static String stringify(Object evaluatedExpression) {
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
}
