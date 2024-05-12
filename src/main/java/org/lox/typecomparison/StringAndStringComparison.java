package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.STRING_TO_DOUBLE_CONVERSION_MAP;

public class StringAndStringComparison implements Compare<String, String> {

    public boolean greater(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) > STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean greaterThan(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) >= STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean less(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) < STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }

    public boolean lessThan(String type1, String type2) {
        return STRING_TO_DOUBLE_CONVERSION_MAP.get(type1.substring(0, 1)) <= STRING_TO_DOUBLE_CONVERSION_MAP.get(type2.substring(0, 1));
    }
}
