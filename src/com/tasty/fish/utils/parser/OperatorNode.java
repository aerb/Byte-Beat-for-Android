package com.tasty.fish.utils.parser;

public class OperatorNode implements IExpressionNode{
    private final FastParse.Op _o;
    private final IExpressionNode _n0;
    private final IExpressionNode _n1;

    public OperatorNode(FastParse.Op o, IExpressionNode n0, IExpressionNode n1) {
        _o = o;
        _n0 = n0;
        _n1 = n1;
    }

    public double eval() {
        return _o.Ex(_n0.eval(), _n1.eval());
    }
}
