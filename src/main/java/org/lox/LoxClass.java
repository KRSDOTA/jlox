package org.lox;

import org.lox.callable.LoxCallable;
import org.lox.callable.LoxFunction;
import org.lox.vistor.Interpreter;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {
   final String name;
   final Map<String, LoxFunction> methods;

   public LoxClass(String name, Map<String, LoxFunction> methods) {
       this.name = name;
       this.methods = methods;
   }

   public LoxFunction findMethod(String name) {
      if(methods.containsKey(name)) {
          return methods.get(name);
      }
      return null;
   }

   @Override
    public String toString() {
      return name;
   }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
      LoxInstance instance = new LoxInstance(this);
      return instance;
    }
}
