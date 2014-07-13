package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.Listener;

public class Expression {

    private int[] _args = { 0, 0, 0 };

    private double _timeDelta = 0;
    private String _expressionString;
    private String _name;
    private boolean _readOnly;
    private boolean _dirty;

    private Listener<Expression> _listener;

    public Expression(
            String name,
            String expression,
            boolean readOnly)
    {
        _name = name;
        _timeDelta = 0.5;
        _args[0] = 50;
        _args[1] = 50;
        _args[2] = 50;
        _expressionString = expression;
        _readOnly = readOnly;
    }

    public String getExpressionString() {
        return _expressionString;
    }

    public void setExpressionString(String expression) {
        _expressionString = expression;
        _dirty = true;
        notifyChange();
    }

    public void setTimeDelta(double inc) {
        _timeDelta = inc;
        notifyChange();
    }

    public void setParameter(int i, int x) {
        if (i < 3 && i >= 0) {
            _args[i] = x;
        }
        notifyChange();
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
        notifyChange();
    }

    private void notifyChange(){
        if(_listener != null) _listener.onEvent(this);
    }

    public void setChangeListener(Listener<Expression> listener){
        _listener = listener;
    }
}
