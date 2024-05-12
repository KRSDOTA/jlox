package org.lox.typecomparison;

import org.lox.scanning.TokenType;

import static org.lox.typecomparison.StringDoubleMappings.stringDoubleHashMap;

public class StringAndDoubleComparison implements Compare<String, Double> {

    public boolean compare(TokenType operator, String string, Double doubleValue) {
        switch (operator){
            case GREATER -> {
                return stringDoubleHashMap.get(string) > doubleValue;
            }
            case GREATER_EQUAL -> {
                return stringDoubleHashMap.get(string) >= doubleValue;
            }
            case LESS -> {
                return doubleValue < stringDoubleHashMap.get(string);
            }
            case LESS_EQUAL -> {
                return  doubleValue <= stringDoubleHashMap.get(string);
            }
            default -> throw new RuntimeException("Attempted Unsupported mixed type Expression operation");
        }
    }


}
