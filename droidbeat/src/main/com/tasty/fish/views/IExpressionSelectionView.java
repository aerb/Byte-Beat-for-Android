package com.tasty.fish.views;

import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.List;

public interface IExpressionSelectionView {
    void update();

    public interface IExpressionSelectionViewListener
    {
        void OnExpressionSelected(int position);
        void OnCancelRequested();
    }

    void addIExpressionSelectionViewListener(IExpressionSelectionViewListener listener);
    void setDataSource(List<ByteBeatExpression> expressions);
}
