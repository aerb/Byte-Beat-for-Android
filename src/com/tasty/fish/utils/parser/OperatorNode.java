package com.tasty.fish.utils.parser;

public class OperatorNode implements IExpressionNode{
    private FastParse.Op o;
    private IExpressionNode p0;
    private IExpressionNode p1;

    public OperatorNode(FastParse.Op o, IExpressionNode p0, IExpressionNode p1) {
        this.o = o;
        this.p0 = p0;
        this.p1 = p1;
    }

    public boolean isFixed() {
        return p0.isFixed() && p1.isFixed();
    }

    public double eval() {
        return o.Ex(p0.eval(), p1.eval());
    }
}
