package com.tasty.fish.domain;

public interface IExpressionEvaluator {
    void setExpression(ByteBeatExpression expression);
    void setTime(int value);
    byte getNextSample();
}
