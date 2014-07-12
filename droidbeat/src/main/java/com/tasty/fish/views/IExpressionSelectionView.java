package com.tasty.fish.views;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;

public interface IExpressionSelectionView {
    void update();
    void setDataSource(List<ByteBeatExpression> expressions);
}
