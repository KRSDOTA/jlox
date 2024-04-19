package org.lox.abstractsyntaxtree;

import org.lox.vistor.Visitor;

public abstract class Expression {

  abstract <T> T accept(Visitor<T> visitor);
}
