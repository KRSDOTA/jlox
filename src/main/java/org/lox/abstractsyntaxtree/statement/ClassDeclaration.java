package org.lox.abstractsyntaxtree.statement;

import org.lox.scanning.Token;
import org.lox.vistor.StatementVisitor;

import java.util.List;

public class ClassDeclaration extends Statement {
    private final Token name;
    private final List<FunctionDeclaration> methods;

    public ClassDeclaration(Token name, List<FunctionDeclaration> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitClassDeclaration(this);
    }

    public Token getName() {
        return name;
    }

    public List<FunctionDeclaration> getMethods() {
        return methods;
    }

}