package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IExpressionSelectionView;

public class ExpressionSelectionPresenter implements
        IExpressionSelectionView.IExpressionSelectionViewListener, IExpressionsRepository.IExpressionsRepositoryListener {
    private IExpressionSelectionView _view;
    private IExpressionsRepository _expressionsRepository;
    private final IAppController _appController;

    public ExpressionSelectionPresenter(
            IExpressionsRepository expressionsRepository,
            IAppController appController) {

        _expressionsRepository = expressionsRepository;
        _expressionsRepository.setIExpressionsRepositoryListener(this);
        _appController = appController;
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

    @Override
    public void OnCancelRequested() {
        _appController.CloseSelector();
    }

    @Override
    public void OnActiveExpressionChanged() {
        _view.update();
    }
}
