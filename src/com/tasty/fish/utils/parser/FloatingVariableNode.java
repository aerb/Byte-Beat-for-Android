package com.tasty.fish.utils.parser;

public class FloatingVariableNode implements IExpressionNode{
    private final MutableFloating _variableValue;

    FloatingVariableNode(MutableFloating d) {
        _variableValue = d;
    }

    public boolean isFixed() {
        return true;
    }

    public double eval() {
        return _variableValue.Value;
    }
}

