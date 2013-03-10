package com.tasty.fish.utils.parser;

public class ValueNode implements IExpressionNode{
    private final int _value;
    ValueNode(int value) {
        _value = value;

    }

    public boolean isFixed() {
        return true;
    }

    public double eval() {
        return _value;
    }
}
