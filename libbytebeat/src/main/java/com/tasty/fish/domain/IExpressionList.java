package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.Expression;

import java.util.List;

public interface IExpressionList {
    void setActive(Expression expression);
    void setActive(int position);
    Expression getActive();

    void add(Expression byteBeatExpression);
    void remove(Expression expression);
    boolean contains(String name);
    boolean hasDirty();
    List<Expression> getExpressions();

    void addActiveChangedListener(Listener<Expression> listener);
    void addExpressionUpdateListener(Listener<Expression> listener);
    void addDataSetChangedListener(Listener<List<Expression>> listener);
}
