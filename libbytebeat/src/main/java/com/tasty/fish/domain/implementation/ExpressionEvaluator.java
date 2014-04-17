package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;
import com.tasty.fish.utils.parser.FastParse;
import com.tasty.fish.utils.parser.utils.MutableFixed;

public class ExpressionEvaluator implements IExpressionEvaluator {

    private ByteBeatExpression _expression;

    private FastParse _parser;
    private MutableFixed _parserTime;
    private MutableFixed[] _parserArgs;

    private double _timeDelta;
    private double _t;

    private int _sampleIndex = 0;
    private int _numSamples = 100;
    private long[] _samplesTimes = new long[_numSamples];

    public ExpressionEvaluator() {
        _parser = new FastParse(ByteBeat.EXPRESSION_PARAMETERS);

        _parserArgs = new MutableFixed[3];
        _parserTime = _parser.getParametersMap().get(ByteBeat.TIME_SYMBOL);

        _parserArgs[0] = _parser.getParametersMap().get(ByteBeat.P0);
        _parserArgs[1] = _parser.getParametersMap().get(ByteBeat.P1);
        _parserArgs[2] = _parser.getParametersMap().get(ByteBeat.P2);

        _t = 0;
    }

    public void setExpression(ByteBeatExpression expression) throws ExpressionParsingException {
        if(expression == null)
            throw new IllegalArgumentException("Expression cannot be null");

        _t = 0;

        _expression = expression;
        _timeDelta = _expression.getTimeDelta();

        _parserArgs[0].Value = _expression.getArgument(0);
        _parserArgs[1].Value = _expression.getArgument(1);
        _parserArgs[2].Value = _expression.getArgument(2);

        _parser.tryParse(expression.getExpressionString());
    }

    public void updateExpression(String e) throws ExpressionParsingException {
        _parser.tryParse(e);
        _expression.setExpressionString(e.trim());
    }

    public byte getNextSample() {
        _t += _timeDelta;
        _parserTime.Value = (long) _t;

        long start = System.nanoTime();
        byte sample = (byte)_parser.evaluate();
        long stop = System.nanoTime();

        _samplesTimes[_sampleIndex++] = (stop - start);
        _sampleIndex = _sampleIndex >= _numSamples ? 0 : _sampleIndex;

        return sample;
    }

    public long getTime() {
        return (long)_t;
    }

    public void updateTimedelta(double t){
        _timeDelta = t;
    }

    public void updateArgument(int i, long x) {
        if (i >= 3 || i < 0)
            return;
        _parserArgs[i].Value = x;
    }

    public void resetTime() {
        _t = 0;
    }

    public long getExecutionTime() {
        long total = 0;
        for(int i=0;i < _numSamples; ++i)
            total += _samplesTimes[i];
        return (total/ _numSamples);
    }
}
