package org.lox.abstractsyntaxtree;

import org.lox.vistor.StatementVisitor;

public abstract class Statement {

    public abstract <T> T accept(StatementVisitor<T> visitor);

}
