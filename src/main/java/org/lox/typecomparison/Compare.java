package org.lox.typecomparison;

import org.lox.scanning.TokenType;

public interface Compare<T, G> {
    boolean compare(TokenType operator, T type1, G type2);
}
