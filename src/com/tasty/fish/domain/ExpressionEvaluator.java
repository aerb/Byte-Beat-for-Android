package com.tasty.fish.domain;

import com.tasty.fish.utils.FastParse;

public class ExpressionEvaluator implements IExpressionEvaluator  {

    private ByteBeatExpression _expression;
    private FastParse _parser;

    private double _timescale;
    private double _t;
    private double[] _args;

    public ExpressionEvaluator() {
        _parser = new FastParse();
        _t = 0;
    }

    @Override
    public void setTime(int value) {
        _t = value;
    }

    public void setExpression(ByteBeatExpression expression){
        if(expression == null)
            throw new IllegalArgumentException("Expression cannot be null");

        _expression = expression;
        _timescale = _expression.getTimeScale();
        _args[0] = _expression.getArguement(0);
        _args[1] = _expression.getArguement(1);
        _args[2] = _expression.getArguement(2);
    }

    public boolean updateExpression(String e) {
        FastParse parser = new FastParse();
        parser.setTime(0);
        parser.setVariable(0, _expression.getArguement(0));
        parser.setVariable(1, _expression.getArguement(1));
        parser.setVariable(2, _expression.getArguement(2));
        if (parser.tryParse(e)) {
            _parser = parser;
            _expression.setExpression(e.trim());
            return true;
        }
        return false;
    }

    public byte getNextSample() {
        _t += _timescale;

        if (!_expression.compiled()) {
            _parser.setTime((long) _t);
            return (byte) _parser.evaluate();
        }

        return (byte) (_expression
                .getCompiled()
                .evaluate(
                        (int) _t,
                        _expression.getArguement(0),
                        _expression.getArguement(1),
                        _expression.getArguement(2)));
    }

    public int getTime() {
        return (int) _t;
    }

    public void updateArguement(int i, double x) {
        if (i < 3 && i >= 0) {
            if (!_expression.compiled())
                _parser.setVariable(i, x);
        }
    }

    public void resetTime() {
        _t = 0;
    }
}
