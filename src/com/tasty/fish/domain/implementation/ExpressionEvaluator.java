package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.utils.parser.ExpressionParsingException;
import com.tasty.fish.utils.parser.FastParse;

public class ExpressionEvaluator implements IExpressionEvaluator {

    private ByteBeatExpression _expression;
    private FastParse _parser;

    private double _timescale;
    private double _t;
    private double[] _args = {1,1,1};

    public ExpressionEvaluator() {
        _parser = new FastParse();
        _t = 0;
    }

    public void setExpression(ByteBeatExpression expression) throws ExpressionParsingException {
        if(expression == null)
            throw new IllegalArgumentException("Expression cannot be null");

        _expression = expression;
        _timescale = _expression.getTimeScale();
        _args[0] = _expression.getArguement(0);
        _args[1] = _expression.getArguement(1);
        _args[2] = _expression.getArguement(2);

        _parser.setVariable(0, _expression.getArguement(0));
        _parser.setVariable(1, _expression.getArguement(1));
        _parser.setVariable(2, _expression.getArguement(2));

        _parser.tryParse(expression.getExpressionAsString());
    }

    public void updateExpression(String e) throws ExpressionParsingException {
        FastParse parser = new FastParse();
        parser.setTime(0);
        parser.setVariable(0, _expression.getArguement(0));
        parser.setVariable(1, _expression.getArguement(1));
        parser.setVariable(2, _expression.getArguement(2));

        parser.tryParse(e);

        _parser = parser;
        _expression.setExpression(e.trim());
    }

    public byte getNextSample() {
        _t += _timescale;
        _parser.setTime((long) _t);
        byte result = (byte) _parser.evaluate();
        return result;
    }

    public int getTime() {
        return (int) _t;
    }

    public void updateTimescale(double t){
        _timescale = t;
    }

    public void updateArguement(int i, double x) {
        if (i >= 3 || i < 0)
            return;
        _parser.setVariable(i, x);
    }

    public void resetTime() {
        _t = 0;
    }
}
