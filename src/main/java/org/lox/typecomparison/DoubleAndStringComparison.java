package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.STRING_TO_DOUBLE_CONVERSION_MAP;

public class DoubleAndStringComparison implements Compare<Double, String> {

    public boolean greater(Double doubleValue, String string) {
      return doubleValue > STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1));
    }

    public boolean greaterThan(Double doubleValue, String string) {
      return doubleValue >= STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1));
    }

    public boolean less(Double doubleValue, String string) {
      return doubleValue < STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1));
    }

    public boolean lessThan(Double doubleValue, String string) {
       return  doubleValue <= STRING_TO_DOUBLE_CONVERSION_MAP.get(string.substring(0, 1));
    }
}


