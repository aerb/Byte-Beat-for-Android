package com.tasty.fish.android;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

public interface IExpressionEventListener {
    void onEvent(ByteBeatExpression expression);
}
