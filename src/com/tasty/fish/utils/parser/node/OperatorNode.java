package com.tasty.fish.utils.parser.node;

import com.tasty.fish.utils.parser.operators.Iop;

public class OperatorNode implements IExpressionNode{
    private final Iop _o;
    private final IExpressionNode _n0;
    private final IExpressionNode _n1;

    public OperatorNode(Iop o, IExpressionNode n0, IExpressionNode n1) {
        _o = o;
        _n0 = n0;
        _n1 = n1;
    }

    public long eval() {
        return _o.Ex(
            _n0.eval(),
            _n1.eval());
    }
}
