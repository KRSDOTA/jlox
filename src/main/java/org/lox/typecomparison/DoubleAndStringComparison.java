package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.stringDoubleHashMap;

public class DoubleAndStringComparison implements Compare<Double, String> {

    public boolean greater(Double doubleValue, String string) {
      return doubleValue > stringDoubleHashMap.get(string);
    }

    public boolean greaterThan(Double doubleValue, String string) {
      return doubleValue >= stringDoubleHashMap.get(string);
    }

    public boolean less(Double doubleValue, String string) {
      return doubleValue < stringDoubleHashMap.get(string);
    }

    public boolean lessThan(Double doubleValue, String string) {
       return  doubleValue <= stringDoubleHashMap.get(string);
    }
}


