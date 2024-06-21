package org.lox.abstractsyntaxtree.statement;

import org.lox.scanning.Token;
import org.lox.vistor.StatementVisitor;

import java.util.List;

public class FunctionDeclaration extends Statement {

    private final Token name;

    private final List<Token> params;

    private final List<Statement> body;

    public FunctionDeclaration(Token name, List<Token> params, List<Statement> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitFunctionDeclaration(this);
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Statement> getBody() {
        return body;
    }
}
