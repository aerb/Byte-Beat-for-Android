package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.ExpressionParsingException;

public interface IExpressionEvaluator {
    void setExpression(ByteBeatExpression expression) throws ExpressionParsingException;
    void updateExpression(String newExpression) throws ExpressionParsingException;
    int getTime();
    byte getNextSample();
    void resetTime();
}
