package com.tasty.fish.domain;

import com.tasty.fish.utils.FastParse;

public class ByteBeatExpression {

    public interface CompiledExpression {
        public byte evaluate(int t, double p1, double p2, double p3);
    }

    private enum ExpressionType {
        dynamic, compiled
    }

    private FastParse _parser;
    private double[] _args = { 0, 0, 0 };
    private float _timescale = 0;
    private String _expression;
    private CompiledExpression _compiled;
    private float _t = 0;
    private String _name;
    private ExpressionType _type;

    public ByteBeatExpression(String name, String expression,
            CompiledExpression e, float timescale, float a1, float a2, float a3) {
        _name = name;
        _timescale = timescale;
        _args[0] = a1;
        _args[1] = a2;
        _args[2] = a3;
        _compiled = e;
        _expression = expression;
        _type = ExpressionType.compiled;
    }

    public ByteBeatExpression(String name, String expression, float timescale,
            float a1, float a2, float a3) {
        _name = name;
        _timescale = timescale;
        _args[0] = a1;
        _args[1] = a2;
        _args[2] = a3;
        _expression = expression;
        _type = ExpressionType.dynamic;
    }

    public String expressionString() {
        return _expression;
    }

    public boolean updateExpression(String e) {
        if (_type == ExpressionType.compiled)
            return false;
        FastParse parser = new FastParse();
        parser.setT(0);
        parser.setVariable(0, _args[0]);
        parser.setVariable(1, _args[1]);
        parser.setVariable(2, _args[2]);
        if (parser.tryParse(e)) {
            _parser = parser;
            _expression = e.trim();
            return true;
        }
        return false;
    }

    public boolean tryParse() {
        _parser = new FastParse();
        _parser.setT(0);
        _parser.setVariable(0, _args[0]);
        _parser.setVariable(1, _args[1]);
        _parser.setVariable(2, _args[2]);
        return _parser.tryParse(_expression);
    }

    public byte getNext() {
        _t += _timescale;
        switch (_type) {
        case dynamic:
            _parser.setT((long)_t);
            return (byte) _parser.evaluate();
        case compiled:
            return (byte) (_compiled.evaluate( (int) _t, _args[0], _args[1],
                    _args[2]));
        }
        return 0;
    }

    public int getTime() {
        return (int) _t;
    }

    public void updateTimeScale(float inc) {
        _timescale = inc;
    }

    public void updateArgument(int i, float x) {
        if (i < 3 && i >= 0) {
            _args[i] = x;
            if (_type == ExpressionType.dynamic)
                _parser.setVariable(i, x);
        }
    }

    public void resetTime() {
        _t = 0;
    }

    public String toString() {
        return _name;
    }

    public float getSpeed() {
        return _timescale;
    }

    public double getArgs(int i) {
        return _args[i];
    }

}
