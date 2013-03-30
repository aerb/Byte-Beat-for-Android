package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.ExpressionParsingException;

public interface IExpressionEvaluator {
    void setExpression(ByteBeatExpression expression) throws ExpressionParsingException;
    void updateExpression(String newExpression) throws ExpressionParsingException;
    int getTime();

    void updateTimescale(double t);
    void updateArguement(int i, double x);

    byte getNextSample();
    void resetTime();
}
