package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.STRING_TO_DOUBLE_CONVERSION_MAP;
import static org.lox.typecomparison.ValueOperations.tryParseDouble;

public class StringAndDoubleComparison implements Compare<String, Double> {

    public boolean greater(String string, Double doubleValue) {
       return tryParseDouble(string)
                .map(aDouble -> aDouble > doubleValue)
                .orElseGet(() -> STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1)) > doubleValue);
    }

    public boolean greaterOrEqual(String string, Double doubleValue) {
        return tryParseDouble(string)
                .map(aDouble -> aDouble >= doubleValue)
                .orElseGet(() -> STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1)) >= doubleValue);
    }

    public boolean less(String string, Double doubleValue) {
        return tryParseDouble(string)
                .map(aDouble -> aDouble >= doubleValue)
                .orElseGet(() -> STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1)) < doubleValue);
    }

   public boolean lessOrEqual(String string, Double doubleValue) {
       return tryParseDouble(string)
               .map(aDouble -> aDouble >= doubleValue)
               .orElseGet(() -> STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1)) <= doubleValue);
    }

}


