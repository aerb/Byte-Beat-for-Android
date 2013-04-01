package com.tasty.fish.utils.parser;

public class FixedVariableNode implements IExpressionNode{
    private final MutableFixed _variableValue;

    FixedVariableNode(MutableFixed i) {
        _variableValue = i;
    }

    public double eval() {
        return _variableValue.Value;
    }
}
