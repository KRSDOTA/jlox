package org.lox.callable;

import org.lox.vistor.AbstractExpressionVisitor;

import java.util.List;

public interface LoxCallable {
    int getArity();
    Object call(AbstractExpressionVisitor interpreter, List<Object> arguments);
}
