package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.Expression;

import java.util.List;

public interface IExpressionList {
    void add(Expression byteBeatExpression);
    void setActive(Expression expression);
    void setActiveExpression(int position);
    void remove(Expression expression);
    boolean contains(String name);
    boolean hasDirty();
    List<Expression> getExpressions();

    Expression getActive();
    public void addActiveChangedListener(Listener<Expression> listener);
    void addExpressionUpdateListener(Listener<Expression> listener);


    void addDataSetChangedListener(Listener<List<Expression>> listener);
}
