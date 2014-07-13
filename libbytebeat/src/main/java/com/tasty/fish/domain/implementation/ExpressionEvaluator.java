package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;
import com.tasty.fish.parser.FastParse;
import com.tasty.fish.parser.utils.ExpressionParsingException;
import com.tasty.fish.parser.utils.ParseArgument;

public class ExpressionEvaluator implements IExpressionEvaluator {

    private FastParse _parser;
    private ParseArgument _parserTime;
    private ParseArgument[] _parserArgs;

    private double _timeDelta;
    private double _t;

    private int _sampleIndex = 0;
    private int _numSamples = 100;
    private long[] _samplesTimes = new long[_numSamples];
    private Expression _expression;

    public ExpressionEvaluator(final IExpressionList expressions) {
        _parser = new FastParse(ByteBeat.EXPRESSION_PARAMETERS);
        _parserArgs = new ParseArgument[3];
        _parserTime = _parser.getParametersMap().get(ByteBeat.TIME_SYMBOL);
        _parserArgs[0] = _parser.getParametersMap().get(ByteBeat.P0);
        _parserArgs[1] = _parser.getParametersMap().get(ByteBeat.P1);
        _parserArgs[2] = _parser.getParametersMap().get(ByteBeat.P2);

        _t = 0;

        expressions.addExpressionUpdateListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
                if(_expression == item)
                    updateParser(_expression);
            }
        });
    }

    public void setExpression(Expression expression) {
        if(expression == null)
            throw new IllegalArgumentException("Expression cannot be null");

        try {
            _t = 0;
            updateParser(expression);
            _parser.tryParse(expression.getExpressionString());
            _expression = expression;
        } catch (ExpressionParsingException e) {
            _parser.clear();
        }
    }

    private void updateParser(Expression expression){
        _timeDelta = expression.getTimeDelta();
        _parserArgs[0].Value = expression.getArgument(0);
        _parserArgs[1].Value = expression.getArgument(1);
        _parserArgs[2].Value = expression.getArgument(2);
    }

    public void tryParse(String e) throws ExpressionParsingException {
        _parser.tryParse(e);
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
