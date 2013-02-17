package com.tasty.fish.domain;

public interface IExpressionEvaluator {
    void setExpression(ByteBeatExpression expression);
    boolean updateExpression(String newExpression);
    void setTime(int value);
    int getTime();
    byte getNextSample();
}
