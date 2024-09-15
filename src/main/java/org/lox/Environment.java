package org.lox;

import org.lox.scanning.Token;
import org.lox.vistor.RuntimeError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Environment {
  public final Map<String, Object> declarationLookup = new HashMap<>();

  private final Optional<Environment> enclosedScope;

  public Environment() {
    enclosedScope = Optional.empty();
  }

  public Environment(Environment enclosedScope) {
   this.enclosedScope = Optional.of(enclosedScope);
  }

  public void define(Token token, Object value) {
    declarationLookup.put(token.lexeme(), value);
  }

  public void define(String string, Object value){
   declarationLookup.put(string, value);
  }

  public Environment getEnclosedScope() {
    return enclosedScope.get();
  }

  public Object getValue(Token token) {
    if(declarationLookup.containsKey(token.lexeme())){
      return declarationLookup.get(token.lexeme());
    }

    return enclosedScope
            .map(environment -> environment.getValue(token))
            .orElseThrow(() -> new RuntimeError(token, "undefined variable specified"));
  }

  public void assign(Token name, Object value) {
    if(declarationLookup.containsKey(name.lexeme())){
      declarationLookup.put(name.lexeme(), value);
      return;
    }

    if(enclosedScope.isPresent()) {
      enclosedScope.get().assign(name, value);
      return;
    }

   throw new RuntimeError(name, "undefined variable '" + name.lexeme() + "'.");
  }

  public Object getAt(int distance, String name) {
    return ancestor(distance).declarationLookup.get(name);
  }

  public void assignAt(int distance, Token name, Object value) {
    ancestor(distance).declarationLookup.put(name.lexeme(), value);
  }

  private Environment ancestor(int distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
     environment = environment.enclosedScope.get();
    }
    return environment;
  }
}
