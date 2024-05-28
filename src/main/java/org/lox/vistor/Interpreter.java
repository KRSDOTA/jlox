package org.lox.vistor;

import org.lox.Environment;
import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.BlockStatement;
import org.lox.abstractsyntaxtree.statement.ExpressionStatement;
import org.lox.abstractsyntaxtree.statement.PrintStatement;
import org.lox.abstractsyntaxtree.statement.Statement;
import org.lox.abstractsyntaxtree.statement.VariableStatement;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.typecomparison.DoubleAndStringComparison;
import org.lox.typecomparison.StringAndDoubleComparison;
import org.lox.typecomparison.StringAndStringComparison;

import java.util.List;

import static org.lox.typecomparison.ValueOperations.*;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();
    private final DoubleAndStringComparison doubleAndStringComparison = new DoubleAndStringComparison();
    private final StringAndDoubleComparison stringAndDoubleComparison = new StringAndDoubleComparison();
    private final StringAndStringComparison stringAndStringComparison = new StringAndStringComparison();

    private Environment globalEnvironment = new Environment();

    public void interpret(List<Statement> statements) {
        try {
            statements.forEach(this::execute);
        } catch (RuntimeError error){
            errorHandler.reportError(error.token, error.getMessage());
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
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
                if(isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return (String) evaluatedLeftExpression + (String) evaluatedRightExpression;
                }
                if(isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)){
                    return ((Double) evaluatedLeftExpression).toString() + (String) evaluatedRightExpression;
                }
                if(isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)){
                    return (String) evaluatedLeftExpression + ((Double) evaluatedRightExpression).toString();
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
    public Object visitVariableExpr(VariableExpression variableExpression) {
        return globalEnvironment.getValue(variableExpression.getToken());
    }

    @Override
    public Object visitAssignmentExpr(AssignmentExpression assignmentExpression) {
      Object value = evaluate(assignmentExpression.getValue());
      globalEnvironment.assign(assignmentExpression.getToken(), value);
      return value;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement expressionStatement) {
        evaluate(expressionStatement.getStatement());
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement printStatement) {
        Object value = evaluate(printStatement.getStatement());
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVariableStatement(VariableStatement variableStatement) {
        Object value = null;
        if(variableStatement.getExpression() != null) {
            value = evaluate(variableStatement.getExpression());
        }
        globalEnvironment.define(variableStatement.getTokenName(), value);
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement blockStatement) {
       executeBlock(blockStatement.getStatements(), new Environment(globalEnvironment));
       return null;
    }

    private void executeBlock(List<Statement> statements, Environment environment) {
        Environment previous = globalEnvironment;
        try {
            this.globalEnvironment = environment;
            statements.forEach(this::execute);
        } finally {
            this.globalEnvironment = previous;
        }
    }
}
