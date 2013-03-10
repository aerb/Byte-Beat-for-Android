package com.tasty.fish.domain;

public class ByteBeatExpression {
    private double[] _args = { 0, 0, 0 };

    private double _timescale = 0;
    private String _expression;
    private float _t = 0;
    private String _name;

    public ByteBeatExpression(
            String name,
            String expression,
            float timescale,
            float a1,
            float a2,
            float a3)
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

    public void setTimeScale(double inc) {
        _timescale = inc;
    }

    public void setArguement(int i, double x) {
        if (i < 3 && i >= 0) {
            _args[i] = x;
        }
    }

    public double getSpeed() {
        return _timescale;
    }

    public double getArguement(int i) {
        return _args[i];
    }

    public double getTimeScale() {
        return _timescale;
    }

    public String getName() {
        return _name;
    }
}
