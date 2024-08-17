package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver implements ExpressionVisitor<Void>, StatementVisitor<Void> {

    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();

    public Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Void visitBinaryExpr(BinaryExpression binaryExpression) {
        return null;
    }

    @Override
    public Void visitGroupingExpr(GroupingExpression groupingExpression) {
        return null;
    }

    @Override
    public Void visitLiteralExpr(LiteralExpression literalExpression) {
        return null;
    }

    @Override
    public Void visitUnaryExpr(UnaryExpression unaryExpression) {
        return null;
    }

    @Override
    public Void visitConditionalExpr(ConditionalExpression conditionalExpression) {
        return null;
    }

    @Override
    public Void visitVariableExpr(VariableExpression variableExpression) {
        return null;
    }

    @Override
    public Void visitAssignmentExpr(AssignmentExpression assignmentExpression) {
        return null;
    }

    @Override
    public Void visitLogicalExpression(LogicalExpression logicalExpression) {
        return null;
    }

    @Override
    public Void visitCallExpression(CallExpression callExpression) {
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement expressionStatement) {
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement printStatement) {
        return null;
    }

    @Override
    public Void visitVariableStatement(VariableStatement variableStatement) {
       declare(variableStatement.getTokenName());
       if(variableStatement.getExpression() != null){
           resolve(variableStatement.getExpression());
       }
       define(variableStatement.getTokenName());
       return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement blockStatement) {
        beginScope();
        resolve(blockStatement.getStatements());
        endScope();
        return null;
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void endScope() {
        scopes.pop();
    }

    private void resolve(List<Statement> statements) {
       statements.forEach(this::resolve);
    }

    private void resolve(Statement statement) {
        statement.accept(this);
    }

    private void resolve(Expression expression) {
        expression.accept(this);
    }

    @Override
    public Void visitIfStatement(IfStatement ifStatement) {
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement whileStatement) {
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        return null;
    }

    @Override
    public Void visitReturnStatement(ReturnStatement returnStatement) {
        return null;
    }
}
