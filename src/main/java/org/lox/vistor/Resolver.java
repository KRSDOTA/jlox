package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.*;
import org.lox.abstractsyntaxtree.statement.*;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.scanning.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Do a pass of the generated AST and perform variable resolution,
 * this means we no longer have to dynamically walk the environment tree every time
 * a variable expression is evaluated. We can pre-calculate the number of "hops" required from the current
 * scope to get to the correct place of resolution.
 */
public class Resolver implements ExpressionVisitor<Void>, StatementVisitor<Void> {

    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();
    private FunctionType currentFunction = FunctionType.NONE;

    public Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public boolean hadError() {
       return errorHandler.hadError();
    }

    @Override
    public Void visitBinaryExpr(BinaryExpression binaryExpression) {
        resolve(binaryExpression.getLeftHandExpression());
        resolve(binaryExpression.getRightHandExpression());
        return null;
    }

    @Override
    public Void visitGroupingExpr(GroupingExpression groupingExpression) {
        resolve(groupingExpression.getGroupedExpression());
        return null;
    }

    @Override
    public Void visitLiteralExpr(LiteralExpression literalExpression) {
        return null;
    }

    @Override
    public Void visitUnaryExpr(UnaryExpression unaryExpression) {
        resolve(unaryExpression.getRightHandExpression());
        return null;
    }

    @Override
    public Void visitConditionalExpr(ConditionalExpression conditionalExpression) {
        resolve(conditionalExpression.getExpression());
        resolve(conditionalExpression.getThenBranch());
        if (conditionalExpression.getElseBranch() != null) {
            resolve(conditionalExpression.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitVariableExpr(VariableExpression variableExpression) {
        if (!scopes.isEmpty() && scopes.peek().get(variableExpression.getToken().lexeme()) == Boolean.FALSE) {
            errorHandler.reportError(variableExpression.getToken(), "Can't read local variable in its own initialiser");
        }
        resolveLocal(variableExpression, variableExpression.getToken());
        return null;
    }

    private void resolveLocal(Expression expression, Token token) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(token.lexeme())){
                interpreter.resolve(expression, scopes.size() - 1 -i);
                return;
            }
        }
    }

    @Override
    public Void visitAssignmentExpr(AssignmentExpression assignmentExpression) {
        resolve(assignmentExpression.getValue());
        resolveLocal(assignmentExpression, assignmentExpression.getToken());
        return null;
    }

    @Override
    public Void visitLogicalExpression(LogicalExpression logicalExpression) {
        resolve(logicalExpression.getLeft());
        resolve(logicalExpression.getRight());
        return null;
    }

    @Override
    public Void visitCallExpression(CallExpression callExpression) {
        resolve(callExpression.getCallee());
        callExpression.getArguments().forEach(this::resolve);
        return null;
    }

    @Override
    public Void visitGetExpression(GetExpression getExpression) {
       resolve(getExpression.getObject());
       return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement expressionStatement) {
        resolve(expressionStatement.getStatement());
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement printStatement) {
        resolve(printStatement.getStatement());
        return null;
    }

    @Override
    public Void visitVariableStatement(VariableStatement variableStatement) {
       declare(variableStatement.getTokenName());
       if (variableStatement.getExpression() != null) {
           resolve(variableStatement.getExpression());
       }
       define(variableStatement.getTokenName());
       return null;
    }

    private void declare(Token tokenName) {
        if (scopes.isEmpty()) {
            return;
        }
        Map<String, Boolean> scope = scopes.peek();
        if (scope.containsKey(tokenName.lexeme())) {
           errorHandler.reportError(tokenName, "Already have a name with this variable in scope");
        }
        scope.put(tokenName.lexeme(), false);
    }

    private void define(Token tokenName) {
        if(scopes.isEmpty()) {
            return;
        }
        scopes.peek().put(tokenName.lexeme(), true);
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

    public void resolve(List<Statement> statements) {
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
        resolve(ifStatement.getCondition());
        resolve(ifStatement.getThenBranch());
        if (ifStatement.getThenBranch() != null) {
           resolve(ifStatement.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement whileStatement) {
        resolve(whileStatement.getCondition());
        resolve(whileStatement.getStatement());
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        declare(functionDeclaration.getName());
        define(functionDeclaration.getName());
        resolveFunction(functionDeclaration, FunctionType.FUNCTION);
        return null;
    }

    private void resolveFunction(FunctionDeclaration functionDeclaration, FunctionType functionType) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = functionType;

        beginScope();
        functionDeclaration.getParams().forEach(param -> {
            declare(param);
            define(param);
        });
        resolve(functionDeclaration.getBody());
        endScope();
        currentFunction = enclosingFunction;
    }

    @Override
    public Void visitReturnStatement(ReturnStatement returnStatement) {
        if (currentFunction == FunctionType.NONE) {
          errorHandler.reportError(returnStatement.getKeyword(), "Can't return from top level code.");
        }
        resolve(returnStatement);
        return null;
    }

    @Override
    public Void visitClassDeclaration(ClassDeclaration classDeclaration) {
        declare(classDeclaration.getName());
        define(classDeclaration.getName());
        return null;
    }
}
