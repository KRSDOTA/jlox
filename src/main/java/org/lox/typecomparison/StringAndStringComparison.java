package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.STRING_TO_DOUBLE_CONVERSION_MAP;
import static org.lox.typecomparison.ValueOperations.tryParseDouble;

public class StringAndStringComparison implements Compare<String, String> {

    public boolean greater(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) > STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean greaterOrEqual(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) >= STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean less(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) < STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean lessOrEqual(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) <= STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }
}
