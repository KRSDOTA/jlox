package org.lox;

import org.lox.callable.LoxFunction;
import org.lox.scanning.Token;
import org.lox.vistor.RuntimeError;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {
    private LoxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public LoxInstance(LoxClass klass) {
       this.klass = klass;
    }

    public Object get(Token name) {
       if (fields.containsKey(name.lexeme())) {
         return fields.get(name.lexeme());
       }

       LoxFunction method = klass.findMethod(name.lexeme());
       if (method != null) {
           return method.bind(this);
       }

       throw new RuntimeError(name, "Undefined property '" + name.lexeme() + "'.");
    }

    public void set(Token fieldName, Object value) {
        fields.put(fieldName.lexeme(), value);
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
