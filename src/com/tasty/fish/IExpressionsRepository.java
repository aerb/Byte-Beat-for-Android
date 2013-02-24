package com.tasty.fish;

import com.tasty.fish.domain.ByteBeatExpression;

import java.util.List;

public interface IExpressionsRepository {
    public interface IExpressionsRepositoryListener
    {
        void OnActiveExpressionChanged();
    }

    void setIExpressionsRepositoryListener(IExpressionsRepositoryListener listener);

    List<ByteBeatExpression> getExpressions();
    ByteBeatExpression getActive();

    void setActiveExpression(int position);
}
