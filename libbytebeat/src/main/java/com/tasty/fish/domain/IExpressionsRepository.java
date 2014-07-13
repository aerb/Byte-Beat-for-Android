package com.tasty.fish.domain;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;


public interface IExpressionsRepository {
    void add(ByteBeatExpression byteBeatExpression);
    void setActiveExpression(ByteBeatExpression expression);
    void setActiveExpression(int position);
    void remove(ByteBeatExpression expression);
    boolean contains(String name);
    List<ByteBeatExpression> getExpressions();
    ByteBeatExpression getActive();

    public void addActiveChangedListener(IChangeListener<ByteBeatExpression> listener);
    void addExpressionUpdateListener(IChangeListener<ByteBeatExpression> listener);
    void addDataSetChangedListener(IChangeListener<List<ByteBeatExpression>> listener);


}
