package org.lox.callable;

import org.lox.Environment;
import org.lox.abstractsyntaxtree.statement.FunctionDeclaration;
import org.lox.vistor.Interpreter;
import org.lox.vistor.Return;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private FunctionDeclaration functionDeclaration;
    private final Environment closure;

    public LoxFunction(FunctionDeclaration functionDeclaration, Environment closure) {
        this.functionDeclaration = functionDeclaration;
        this.closure = closure;
    }

    public int getArity() {
        return functionDeclaration.getParams().size();
    }

    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);

        for (int i = 0; i < arguments.size(); i++) {
            environment.define(functionDeclaration.getParams().get(i), arguments.get(i));
        }
        try {
            interpreter.executeBlock(functionDeclaration.getBody(), environment);
        } catch(Return returnValue){
            return returnValue.getValue();
        }
        return null;
    }

    @Override
    public String toString(){
        return "<fn " + functionDeclaration.getName().lexeme() + ">";
    }

}
