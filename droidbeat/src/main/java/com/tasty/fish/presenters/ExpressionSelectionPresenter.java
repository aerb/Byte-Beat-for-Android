package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IExpressionSelectionView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExpressionSelectionPresenter implements
        IExpressionSelectionView.IExpressionSelectionViewListener, IExpressionsRepository.IExpressionsRepositoryListener {
    private final ExpressionIO _io;
    private IExpressionSelectionView _view;
    private IExpressionsRepository _expressionsRepository;
    private final IAppController _appController;

    public ExpressionSelectionPresenter(
            ExpressionIO io,
            IExpressionsRepository expressionsRepository,
            IAppController appController) {

        _io = io;
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

    public void save(ByteBeatExpression expression) throws IOException {
        _io.save(expression);
    }

    public void delete(ByteBeatExpression expression) throws IOException {
        _expressionsRepository.getExpressions().remove(expression);
        _io.delete(expression);
    }
}
