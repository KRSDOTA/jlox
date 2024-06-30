package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.scanning.Token;
import org.lox.vistor.StatementVisitor;

public class ReturnStatement extends Statement {
   Token keyword;
   Expression value;

   public ReturnStatement(Token keyword, Expression value){
       this.keyword = keyword;
       this.value = value;
   }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitReturnStatement(this);
    }

    public Expression getValue() {
        return value;
    }

    public Token getKeyword() {
        return keyword;
    }
}
