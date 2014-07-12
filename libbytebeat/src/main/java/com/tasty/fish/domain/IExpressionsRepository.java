package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;


public interface IExpressionsRepository {
    void addNewExpression(ByteBeatExpression byteBeatExpression);

    void setActiveExpression(ByteBeatExpression expression);

    boolean contains(String name);

    void setActiveChangedListener(IExpressionListener listener);

    List<ByteBeatExpression> getExpressions();
    ByteBeatExpression getActive();

    void setActiveExpression(int position);

    void updateActive(String text);
}
