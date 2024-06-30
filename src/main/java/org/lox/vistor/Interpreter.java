package org.lox.vistor;

import org.lox.Environment;
import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.*;
import org.lox.callable.LoxCallable;
import org.lox.callable.LoxFunction;
import org.lox.scanning.TokenType;
import org.lox.typecomparison.DoubleAndStringComparison;
import org.lox.typecomparison.StringAndDoubleAddition;
import org.lox.typecomparison.StringAndDoubleComparison;
import org.lox.typecomparison.StringAndStringComparison;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;

import java.util.ArrayList;
import java.util.List;

import static org.lox.typecomparison.ValueOperations.*;

public class Interpreter implements StatementVisitor<Void>, ExpressionVisitor<Object> {

    public Environment globals = new Environment();
    public Environment environments = globals;

    private final DoubleAndStringComparison doubleAndStringComparison = new DoubleAndStringComparison();
    private final StringAndDoubleComparison stringAndDoubleComparison = new StringAndDoubleComparison();
    private final StringAndStringComparison stringAndStringComparison = new StringAndStringComparison();
    private final StringAndDoubleAddition stringAndDoubleAddition = new StringAndDoubleAddition();
    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();

    public Interpreter() {
        globals.define("clock", new LoxCallable() {
            @Override
            public int getArity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native function>";
            }
        });
    }

    public void interpret(List<Statement> statements) {
        try {
            statements.forEach(this::execute);
        } catch (RuntimeError error) {
            errorHandler.reportError(error.token, error.getMessage());
        }
    }

    @Override
    public Object visitBinaryExpr(BinaryExpression expr) {
        Object evaluatedLeftExpression = evaluate(expr.getLeftHandExpression());
        Object evaluatedRightExpression = evaluate(expr.getRightHandExpression());

        switch (expr.getOperator().tokenType()) {
            case MINUS -> {
                checkNumberOperand(expr.getOperator(), evaluatedRightExpression);
                return (double) evaluatedLeftExpression - (double) evaluatedRightExpression;
            }
            case SLASH -> {
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression / (double) evaluatedRightExpression;
            }
            case STAR -> {
                checkNumberOperand(expr.getOperator(), evaluatedRightExpression);
                return (double) evaluatedLeftExpression * (double) evaluatedRightExpression;
            }
            case PLUS -> {
                if (isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return (String) evaluatedLeftExpression + (String) evaluatedRightExpression;
                }
                if (isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleAddition.addStringAndDouble((String) evaluatedRightExpression,
                            (Double) evaluatedLeftExpression);
                }
                if (isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleAddition.addStringAndDouble((String) evaluatedLeftExpression,
                            (Double) evaluatedRightExpression);
                }
                if (evaluatedLeftExpression instanceof Double && evaluatedRightExpression instanceof Double) {
                    return (double) evaluatedLeftExpression + (double) evaluatedRightExpression;
                }
                throw new RuntimeError(expr.getOperator(), "Operands must be two strings");
            }
            case GREATER -> {
                if (isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleComparison.greater((String) evaluatedLeftExpression,
                            (Double) evaluatedRightExpression);
                }
                if (isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return doubleAndStringComparison.greater((Double) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                if (isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndStringComparison.greater((String) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression > (double) evaluatedRightExpression;
            }
            case GREATER_EQUAL -> {
                if (isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleComparison.greaterOrEqual((String) evaluatedLeftExpression,
                            (Double) evaluatedRightExpression);
                }
                if (isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return doubleAndStringComparison.greaterOrEqual((Double) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                if (isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndStringComparison.greaterOrEqual((String) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression >= (double) evaluatedRightExpression;
            }
            case LESS -> {
                if (isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleComparison.less((String) evaluatedLeftExpression,
                            (Double) evaluatedRightExpression);
                }
                if (isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return doubleAndStringComparison.less((Double) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                if (isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndStringComparison.less((String) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression < (double) evaluatedRightExpression;
            }
            case LESS_EQUAL -> {
                if (isOperandsStringAndDouble(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndDoubleComparison.lessOrEqual((String) evaluatedLeftExpression,
                            (Double) evaluatedRightExpression);
                }
                if (isOperandsDoubleAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return doubleAndStringComparison.lessOrEqual((Double) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                if (isOperandsStringAndString(evaluatedLeftExpression, evaluatedRightExpression)) {
                    return stringAndStringComparison.lessOrEqual((String) evaluatedLeftExpression,
                            (String) evaluatedRightExpression);
                }
                checkNumberOperands(expr.getOperator(), evaluatedLeftExpression, evaluatedRightExpression);
                return (double) evaluatedLeftExpression <= (double) evaluatedRightExpression;
            }
            case BANG_EQUAL -> {
                return !isEqual(evaluatedLeftExpression, evaluatedRightExpression);
            }
            case EQUAL_EQUAL -> {
                return isEqual(evaluatedLeftExpression, evaluatedRightExpression);
            }
            case COMMA -> {
                return evaluatedRightExpression;
            } // discard left expression without evaluating?
            default -> throw new Error("Opps");
        }
    }

    protected Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    public Object visitGroupingExpr(GroupingExpression expr) {
        return evaluate(expr.getGroupedExpression());
    }

    public Object visitLiteralExpr(LiteralExpression expr) {
        return expr.getValue();
    }

    public Object visitUnaryExpr(UnaryExpression expr) {
        Object right = evaluate(expr);

        switch (expr.getOperator().tokenType()) {
            case MINUS -> {
                return -(double) right;
            }
            case BANG -> {
                return !isTruthy(right);
            }
        }

        return null;
    }

    public Object visitConditionalExpr(ConditionalExpression expr) {
        Object conditional = evaluate(expr.getExpression());
        if (isTruthy(conditional)) {
            return evaluate(expr.getThenBranch());
        }

        return evaluate(expr.getElseBranch());
    }

    public Object visitLogicalExpression(LogicalExpression logicalExpression) {
        Object left = evaluate(logicalExpression.getLeft());
        if (logicalExpression.getOperator().tokenType() == TokenType.OR) {
            if (isTruthy(left)) {
                return left;
            }
        } else if (logicalExpression.getOperator().tokenType() == TokenType.AND) {
            if (!isTruthy(left)) {
                return left;
            }
        }
        return evaluate(logicalExpression.getRight());
    }

    @Override
    public Object visitCallExpression(CallExpression callExpression) {
        Object callee = evaluate(callExpression.getCallee());

        List<Object> arguments = new ArrayList<>();

        callExpression.getArguments().forEach(arg -> arguments.add(evaluate(arg)));

        if (!(callee instanceof LoxCallable)) {
            throw new RuntimeError(callExpression.getParen(), "Can only call functions and classes");
        }

        LoxCallable function = (LoxCallable) callee;

        if (arguments.size() != function.getArity()) {
            throw new RuntimeError(
                    callExpression.getParen(),
                    "Expected " + function.getArity() + " arguments but got " + arguments.size() + ".");
        }

        return function.call(this, arguments);
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    @Override
    public Object visitVariableExpr(VariableExpression variableExpression) {
        return globals.getValue(variableExpression.getToken());
    }

    @Override
    public Object visitAssignmentExpr(AssignmentExpression assignmentExpression) {
        Object value = evaluate(assignmentExpression.getValue());
        globals.assign(assignmentExpression.getToken(), value);
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
        if (variableStatement.getExpression() != null) {
            value = evaluate(variableStatement.getExpression());
        }
        globals.define(variableStatement.getTokenName(), value);
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement blockStatement) {
        executeBlock(blockStatement.getStatements(), new Environment(globals));
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement ifStatement) {
        Object conditionalValue = evaluate(ifStatement.getCondition());
        if (isTruthy(conditionalValue)) {
            execute(ifStatement.getThenBranch());
        } else {
            execute(ifStatement.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement whileStatement) {
        while (isTruthy(evaluate(whileStatement.getCondition()))) {
            execute(whileStatement.getStatement());
        }
        return null;
    }

    public void executeBlock(List<Statement> statements, Environment environment) {
        Environment previous = globals;
        try {
            this.globals = environment;
            statements.forEach(this::execute);
        } finally {
            this.globals = previous;
        }
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        LoxFunction function = new LoxFunction(functionDeclaration);
        environments.define(functionDeclaration.getName(), function);
        return null;
    }

    @Override
    public Void visitReturnStatement(ReturnStatement returnStatement) {
        Object value = null;

        if(returnStatement.getValue() != null){
            value = evaluate(returnStatement.getValue());
        }

        throw new Return(value);
    }


}
