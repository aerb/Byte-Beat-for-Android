package com.tasty.fish.parser.node;

public class ValueNode implements IExpressionNode{
    private final long _value;

    public ValueNode(long value) {
        _value = value;
    }

    public long eval() {
        return _value;
    }
}
