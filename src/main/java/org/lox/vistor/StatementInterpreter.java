package org.lox.vistor;

import org.lox.Environment;
import org.lox.abstractsyntaxtree.expression.AssignmentExpression;
import org.lox.abstractsyntaxtree.expression.VariableExpression;
import org.lox.abstractsyntaxtree.statement.*;
import org.lox.callable.LoxCallable;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;

import java.util.List;

import static org.lox.typecomparison.ValueOperations.isTruthy;
import static org.lox.typecomparison.ValueOperations.stringify;

public class StatementInterpreter extends AbstractExpressionVisitor implements StatementVisitor<Void> {

    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();

    private Environment globals = new Environment();
    private Environment environments = globals;

    public StatementInterpreter(){
        globals.define("clock", new LoxCallable() {
            @Override
            public int getArity() {
                return 0;
            }

            @Override
            public Object call(AbstractExpressionVisitor interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString(){
                return "<native function>";
            }
        });
    }

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
        if(variableStatement.getExpression() != null) {
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
        while(isTruthy(evaluate(whileStatement.getCondition()))){
            execute(whileStatement.getStatement());
        }
        return null;
    }

    private void executeBlock(List<Statement> statements, Environment environment) {
        Environment previous = globals;
        try {
            this.globals= environment;
            statements.forEach(this::execute);
        } finally {
            this.globals= previous;
        }
    }
}
