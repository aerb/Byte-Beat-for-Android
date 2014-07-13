package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;

public interface IExpressionEvaluator {

    void setExpression(Expression expression) throws ExpressionParsingException;
    void tryParse(String newExpression) throws ExpressionParsingException;
    long getTime();

    void updateTimedelta(double t);
    void updateArgument(int i, long x);

    byte getNextSample();

    void resetTime();

    long getExecutionTime();
}