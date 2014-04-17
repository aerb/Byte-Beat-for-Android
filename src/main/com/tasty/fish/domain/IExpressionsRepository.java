package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;

public interface IExpressionsRepository {
    void addNewExpression(ByteBeatExpression byteBeatExpression);

    void setActiveExpressionLast();

    public interface IExpressionsRepositoryListener
    {
        void OnActiveExpressionChanged();
    }

    void setIExpressionsRepositoryListener(IExpressionsRepositoryListener listener);

    List<ByteBeatExpression> getExpressions();
    ByteBeatExpression getActive();

    void setActiveExpression(int position);
}
