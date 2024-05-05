package org.lox.vistor;

import org.lox.scanning.Token;

public class RuntimeError extends RuntimeException {
    final Token token;
    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
