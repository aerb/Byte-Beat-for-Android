package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionListener;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IExpressionSelectionView;

import java.io.IOException;

public class ExpressionSelectionPresenter implements
        IExpressionListener
{
    private final ExpressionIO _io;
    private IExpressionSelectionView _view;
    private IExpressionsRepository _repo;
    private final IAppController _appController;

    public ExpressionSelectionPresenter(
        ExpressionIO io,
        IExpressionsRepository expressionsRepository,
        IAppController appController) {
        _io = io;
        _repo = expressionsRepository;
        _repo.setActiveChangedListener(this);
        _appController = appController;
    }

    public void setView(IExpressionSelectionView view){
        _view = view;
        _view.setDataSource(_repo.getExpressions());
    }

    @Override
    public void onExpressionEvent() {
        _view.update();
    }

    public void save(ByteBeatExpression expression) throws IOException {
        _io.save(expression);
    }

    public void delete(ByteBeatExpression expression) throws IOException {
        _repo.getExpressions().remove(expression);
        _view.update();
        _io.delete(expression);
    }

    public void selectExpression(ByteBeatExpression expression) {
        _repo.setActiveExpression(expression);
    }

    public void select(int index){
        _repo.setActiveExpression(index);
    }

    public void exitView() {
        _appController.CloseSelector();
    }
}
