package com.tasty.fish;

import com.tasty.fish.domain.ByteBeatExpression;

import java.util.List;

public interface IExpressionsRepository {
    List<ByteBeatExpression> getExpressions();
}
