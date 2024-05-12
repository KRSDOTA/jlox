package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.stringDoubleHashMap;

public class StringAndDoubleComparison implements Compare<String, Double> {

    public boolean greater(String string, Double doubleValue) {
       return stringDoubleHashMap.get(string.substring(0, 1)) > doubleValue;
    }

    public boolean greaterThan(String string, Double doubleValue) {
       return stringDoubleHashMap.get(string.substring(0, 1)) >= doubleValue;
    }

    public boolean less(String string, Double doubleValue){
      return stringDoubleHashMap.get(string.substring(0, 1)) < doubleValue;
    }

   public boolean lessThan(String string, Double doubleValue) {
      return stringDoubleHashMap.get(string.substring(0, 1)) <= doubleValue;
   }
 }


