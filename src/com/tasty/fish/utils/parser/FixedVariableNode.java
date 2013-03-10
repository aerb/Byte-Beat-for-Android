package com.tasty.fish.utils.parser;

public class FixedVariableNode implements IExpressionNode{
    private final MutableFixed _variableValue;

    FixedVariableNode(MutableFixed i) {
        _variableValue = i;
    }

    public boolean isFixed() {
        return true;
    }

    public double eval() {
        return _variableValue.Value;
    }
}
