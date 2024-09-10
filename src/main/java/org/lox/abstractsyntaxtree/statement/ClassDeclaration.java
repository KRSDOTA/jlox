package org.lox.abstractsyntaxtree.statement;

import org.lox.abstractsyntaxtree.expression.VariableExpression;
import org.lox.scanning.Token;
import org.lox.vistor.StatementVisitor;

import java.util.List;

public class ClassDeclaration extends Statement {
    private final Token name;
    private final List<FunctionDeclaration> methods;
    private final VariableExpression superclass;

    public ClassDeclaration(Token name, List<FunctionDeclaration> methods, VariableExpression superclass) {
        this.name = name;
        this.methods = methods;
        this.superclass = superclass;
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

    public VariableExpression getSuperclass() {
        return superclass;
    }
}