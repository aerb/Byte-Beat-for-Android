package com.tasty.fish.parser.node;

import com.tasty.fish.parser.utils.ParseArgument;

public class VariableNode implements IExpressionNode{
    private final ParseArgument _variableValue;

    public VariableNode(ParseArgument d) {
        _variableValue = d;
    }

    public long eval() {
        return _variableValue.Value;
    }
}

