package org.lox.abstractsyntaxtree;

import org.lox.vistor.StatementVisitor;

public abstract class Statement {

    abstract <T> T accept(StatementVisitor<T> visitor);

}
