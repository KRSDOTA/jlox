package org.lox.vistor;

import org.lox.abstractsyntaxtree.expression.AssignmentExpression;
import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.abstractsyntaxtree.expression.VariableExpression;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;

import static org.lox.typecomparison.ValueOperations.stringify;

public class SingleExpressionInterpreter extends AbstractExpressionVisitor {

    private final JLoxErrorHandler errorHandler = new JLoxLexerErrorHandler();

    public void interpret(Expression expression) {
       try {
          final Object value = evaluate(expression);
          System.out.println(stringify(value));
       } catch (RuntimeError error){
           errorHandler.reportError(error.token, error.getMessage());
       }
    }

    @Override
    public Object visitVariableExpr(VariableExpression variableExpression) {
        return null;
    }

    @Override
    public Object visitAssignmentExpr(AssignmentExpression assignmentExpression) {
        return null;
    }
}
