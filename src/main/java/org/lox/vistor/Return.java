package org.lox.vistor;

public class Return extends RuntimeException {
    private final Object value;

    Return(Object value){
        super(null, null, false, false);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
