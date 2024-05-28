package org.lox.typecomparison;

import java.util.Optional;

import static org.lox.typecomparison.ValueOperations.tryParseDouble;

public class StringAndDoubleAddition {

    public Object addStringAndDouble(String string, Double doubleToAdd) {
        Optional<Double> parsedString = tryParseDouble(string);
        if (parsedString.isPresent()) {
            return parsedString.get() + doubleToAdd;
        }

        return string + doubleToAdd;
    }
}
