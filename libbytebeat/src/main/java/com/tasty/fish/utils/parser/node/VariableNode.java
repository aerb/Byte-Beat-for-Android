package com.tasty.fish.utils.parser.node;

import com.tasty.fish.utils.parser.utils.MutableFixed;

public class VariableNode implements IExpressionNode{
    private final MutableFixed _variableValue;

    public VariableNode(MutableFixed d) {
        _variableValue = d;
    }

    public long eval() {
        return _variableValue.Value;
    }
}

