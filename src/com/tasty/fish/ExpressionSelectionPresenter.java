package com.tasty.fish;

public class ExpressionSelectionPresenter implements IExpressionSelectionView.IExpressionSelectionViewListener {
    private IExpressionSelectionView _view;
    private IExpressionsRepository _expressionsRepository;

    public ExpressionSelectionPresenter(IExpressionsRepository expressionsRepository) {
        _expressionsRepository = expressionsRepository;
    }

    public void setView(IExpressionSelectionView view){
        _view = view;
        _view.setDataSource(_expressionsRepository.getExpressions());
        _view.addIExpressionSelectionViewListener(this);
    }

    @Override
    public void OnExpressionSelected(int position) {
        _expressionsRepository.setActiveExpression(position);
    }
}
