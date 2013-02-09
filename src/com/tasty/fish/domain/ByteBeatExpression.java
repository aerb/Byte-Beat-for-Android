package com.tasty.fish.domain;

public class ByteBeatExpression {

    public String getName() {
        return _name;
    }

    public interface CompiledExpression {

        public byte evaluate(int t, double p1, double p2, double p3);
    }

    private enum ExpressionType {
        dynamic, compiled;
    }
    private double[] _args = { 0, 0, 0 };

    private double _timescale = 0;
    private String _expression;
    private CompiledExpression _compiledExpression;
    private float _t = 0;
    private ExpressionType _type;
    private String _name;

    public ByteBeatExpression(
            String name,
            String expression,
            float timescale,
            float a1,
            float a2,
            float a3)
    {
        this(name,expression, timescale, a1, a2, a3, null);
    }

    public ByteBeatExpression(
            String name,
            String expression,
            float timescale,
            float a1,
            float a2,
            float a3,
            CompiledExpression compiledExpression)
    {
        _name = name;
        _timescale = timescale;
        _args[0] = a1;
        _args[1] = a2;
        _args[2] = a3;
        _expression = expression;
        _compiledExpression = compiledExpression;
        _type = _compiledExpression != null ?
                ExpressionType.compiled :
                ExpressionType.dynamic;
    }

    public String getExpression() {
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

    public boolean compiled() {
        return _type == ExpressionType.compiled;
    }

    public CompiledExpression getCompiled() {
        return _compiledExpression;
    }
}
