package com.tasty.fish.domain.implementation;

public class ByteBeatExpression {
    private int[] _args = { 0, 0, 0 };

    private double _timescale = 0;
    private String _expression;
    private double _t = 0;
    private String _name;

    public ByteBeatExpression(
            String name,
            String expression,
            float timescale,
            int a1,
            int a2,
            int a3)
    {
        _name = name;
        _timescale = timescale;
        _args[0] = a1;
        _args[1] = a2;
        _args[2] = a3;
        _expression = expression;
    }

    public String getExpressionAsString() {
        return _expression;
    }

    public void setExpression(String expression) {
        _expression = expression;
    }

    public void setTimescale(double inc) {
        _timescale = inc;
    }

    public void setParameter(int i, int x) {
        if (i < 3 && i >= 0) {
            _args[i] = x;
        }
    }

    public double getSpeed() {
        return _timescale;
    }

    public int getArgument(int i) {
        return _args[i];
    }

    public double getTimeScale() {
        return _timescale;
    }

    public String getName() {
        return _name;
    }

    public void resetParametersAndTimescale() {
        setTimescale(0.5f);
        setParameter(0, 1);
        setParameter(1, 1);
        setParameter(2, 1);
    }
}
