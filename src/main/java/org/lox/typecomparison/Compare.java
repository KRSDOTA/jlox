package org.lox.typecomparison;

public interface Compare<T, G> {

    boolean greater(T type1, G type2);
    boolean greaterThan(T type1, G type2);
    boolean less(T type1, G type2);
    boolean lessThan(T type1, G type2);

}
