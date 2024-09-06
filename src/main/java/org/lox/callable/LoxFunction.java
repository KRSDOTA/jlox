package org.lox.callable;

import org.lox.Environment;
import org.lox.LoxInstance;
import org.lox.abstractsyntaxtree.statement.FunctionDeclaration;
import org.lox.vistor.Interpreter;
import org.lox.vistor.Return;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private FunctionDeclaration functionDeclaration;
    private final Environment closure;
    private final boolean isInitaliser;

    public LoxFunction(FunctionDeclaration functionDeclaration, Environment closure, boolean isInitaliser) {
        this.functionDeclaration = functionDeclaration;
        this.closure = closure;
        this.isInitaliser = isInitaliser;
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
        } catch(Return returnValue) {
            if(isInitaliser){
                return closure.getAt(0, "this");
            }
            return returnValue.getValue();
        }
        if(isInitaliser) {
            return closure.getAt(0, "this");
        }
        return null;
    }

    public LoxFunction bind(LoxInstance instance) {
       Environment environment = new Environment(closure);
       environment.define("this", instance);
       return new LoxFunction(functionDeclaration, environment, isInitaliser);
    }

    @Override
    public String toString(){
        return "<fn " + functionDeclaration.getName().lexeme() + ">";
    }

}
