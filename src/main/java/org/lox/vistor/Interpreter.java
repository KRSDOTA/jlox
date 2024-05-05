package org.lox.vistor;

import org.lox.abstractsyntaxtree.*;

public class Interpreter implements ExpressionVisitor<Object> {

    @Override
    public Object visitBinaryExpr(BinaryExpression expr) {
        Object leftExpression = evaluate(expr.getLeftHandExpression());
        Object rightExpression = evaluate(expr.getRightHandExpression());

        switch (expr.getOperator().tokenType()){
            case MINUS -> { return (double) leftExpression - (double) rightExpression; }
            case SLASH -> { return (double) leftExpression /  (double) rightExpression; }
            case STAR -> { return (double) leftExpression *  (double) rightExpression; }
            default -> throw new Error("Opps");
        }

    }

    @Override
    public Object visitGroupingExpr(GroupingExpression expr) {
       return evaluate(expr);
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
        return null;
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }
}
