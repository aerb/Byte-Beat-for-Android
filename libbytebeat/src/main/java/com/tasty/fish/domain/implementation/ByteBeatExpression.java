package com.tasty.fish.domain.implementation;

public class ByteBeatExpression {


    private int[] _args = { 0, 0, 0 };

    private double _timeDelta = 0;
    private String _expressionString;
    private String _name;
    private boolean _readOnly;
    private boolean _dirty;

    public ByteBeatExpression(
            String name,
            String expression,
            double timeDelta,
            int a1,
            int a2,
            int a3,
            boolean readOnly)
    {
        _name = name;
        _timeDelta = timeDelta;
        _args[0] = a1;
        _args[1] = a2;
        _args[2] = a3;
        _expressionString = expression;
        _readOnly = readOnly;
    }

    public String getExpressionString() {
        return _expressionString;
    }

    public void setExpressionString(String expression) {
        _expressionString = expression;
        _dirty = true;
    }

    public void setTimeDelta(double inc) {
        _timeDelta = inc;
    }

    public void setParameter(int i, int x) {
        if (i < 3 && i >= 0) {
            _args[i] = x;
        }
    }

    public double getTimeDelta() {
        return _timeDelta;
    }

    public int getArgument(int i) {
        return _args[i];
    }

    public String getName() {
        return _name;
    }

    public void resetParametersAndTimeDelta() {
        setTimeDelta(0.5f);
        setParameter(0, 50);
        setParameter(1, 50);
        setParameter(2, 50);
    }

    public boolean isReadOnly() {
        return _readOnly;
    }

    public boolean isDirty() {
        return _dirty;
    }

    public void setIsDirty(boolean dirty) {
        _dirty = dirty;
    }
}
