package com.tasty.fish.domain;

public interface IExpressionEvaluator {
    void setExpression(ByteBeatExpression expression);
    boolean updateExpression(String newExpression);
    int getTime();
    byte getNextSample();
    void resetTime();
}
