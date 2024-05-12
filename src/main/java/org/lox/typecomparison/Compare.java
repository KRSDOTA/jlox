package org.lox.typecomparison;

public interface Compare<T, G> {

    boolean greater(T type1, G type2);
    boolean greaterOrEqual(T type1, G type2);
    boolean less(T type1, G type2);
    boolean lessOrEqual(T type1, G type2);

}
