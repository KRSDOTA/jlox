package org.lox.vistor;

import org.lox.scanning.Token;

public class VariableUsage {

    private Token token;
    private boolean used = false;

    public VariableUsage(Token token) {
        this.token = token;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Token getToken() {
        return token;
    }

    public String getLexeme() {
       return token.lexeme();
    }

   public boolean isUsed() {
        return used;
   }
}
