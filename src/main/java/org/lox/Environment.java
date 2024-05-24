package org.lox;

import org.lox.scanning.Token;
import org.lox.vistor.RuntimeError;

import java.util.HashMap;
import java.util.Map;

public class Environment {
  public final Map<String, Object> declarationLookup = new HashMap<>();

  public void define(Token token, Object value) {
    declarationLookup.put(token.lexeme(), value);
  }

  public Object getValue(Token token) {
    if(declarationLookup.containsKey(token.lexeme())){
      return declarationLookup.get(token.lexeme());
    }

    throw new RuntimeError(token, "undefined variable specified");
  }

  public void assign(Token name, Object value) {
    if(declarationLookup.containsKey(name.lexeme())){
      declarationLookup.put(name.lexeme(), value);
      return;
    }
   throw new RuntimeError(name, "undefined variable '" + name.lexeme() + "'.");
  }
}
