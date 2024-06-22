package org.lox.callable;

import org.lox.vistor.Interpreter;

import java.util.List;

public interface LoxCallable {
    int getArity();

    Object call(Interpreter interpreter, List<Object> arguments);
}
