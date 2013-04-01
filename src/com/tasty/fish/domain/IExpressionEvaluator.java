package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;

public interface IExpressionEvaluator {

    void setExpression(ByteBeatExpression expression) throws ExpressionParsingException;
    void updateExpression(String newExpression) throws ExpressionParsingException;
    long getTime();

    void updateTimedelta(double t);
    void updateArgument(int i, long x);

    byte getNextSample();

    void resetTime();

    long getExecutionTime();
}