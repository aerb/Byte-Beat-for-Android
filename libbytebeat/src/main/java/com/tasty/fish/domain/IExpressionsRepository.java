package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.Expression;

import java.util.List;


public interface IExpressionsRepository {
    void add(Expression byteBeatExpression);
    void setActiveExpression(Expression expression);
    void setActiveExpression(int position);
    void remove(Expression expression);
    boolean contains(String name);
    List<Expression> getExpressions();
    Expression getActive();

    public void addActiveChangedListener(Listener<Expression> listener);
    void addExpressionUpdateListener(Listener<Expression> listener);
    void addDataSetChangedListener(Listener<List<Expression>> listener);


}
