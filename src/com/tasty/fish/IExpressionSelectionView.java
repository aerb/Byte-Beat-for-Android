package com.tasty.fish;

import com.tasty.fish.domain.ByteBeatExpression;

import java.util.List;

public interface IExpressionSelectionView {
    public interface IExpressionSelectionViewListener
    {
        void OnExpressionSelected(int position);
    }

    void addIExpressionSelectionViewListener(IExpressionSelectionViewListener listener);
    void setDataSource(List<ByteBeatExpression> expressions);
}
