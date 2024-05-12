package org.lox.typecomparison;

import static org.lox.typecomparison.StringDoubleMappings.stringDoubleHashMap;

public class StringAndDoubleComparison implements Compare<String, Double> {

    public boolean greater(String string, Double doubleValue) {
       return stringDoubleHashMap.get(string) > doubleValue;
    }

    public boolean greaterThan(String string, Double doubleValue) {
       return stringDoubleHashMap.get(string) >= doubleValue;
    }

    public boolean less(String string, Double doubleValue){
      return doubleValue < stringDoubleHashMap.get(string);
    }

   public boolean lessThan(String string, Double doubleValue) {
      return  doubleValue <= stringDoubleHashMap.get(string);
   }

 }


