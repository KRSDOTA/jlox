package org.lox.typecomparison;

import org.lox.scanning.TokenType;

import static org.lox.typecomparison.StringDoubleMappings.stringDoubleHashMap;

public class DoubleAndStringComparison implements Compare<Double, String> {

    public boolean compare(TokenType operator, Double doubleValue, String string) {
        switch (operator){
            case GREATER -> {
                return doubleValue > stringDoubleHashMap.get(string);
            }
            case GREATER_EQUAL -> {
                return doubleValue >= stringDoubleHashMap.get(string);
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
