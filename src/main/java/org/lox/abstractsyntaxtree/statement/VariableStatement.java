package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.scanning.Token;
import org.lox.vistor.StatementVisitor;

public class VariableStatement extends Statement {
  private final Expression expression;

  private final Token tokenName;

  public VariableStatement(Expression expression, Token tokenName) {
    this.expression = expression;
    this.tokenName = tokenName;
  }

  @Override
  public <T> T accept(StatementVisitor<T> visitor) {
    return visitor.visitVariableStatement(this);
  }

  public Expression getExpression() {
    return expression;
  }

  public Token getTokenName() {
    return tokenName;
  }
}