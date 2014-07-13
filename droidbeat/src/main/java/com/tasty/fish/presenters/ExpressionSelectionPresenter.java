package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.views.IAppController;

import java.io.IOException;

public class ExpressionSelectionPresenter
{
    private final ExpressionIO _io;
    private IExpressionsRepository _repo;
    private final IAppController _appController;

    public ExpressionSelectionPresenter(
        ExpressionIO io,
        IExpressionsRepository expressionsRepository,
        IAppController appController) {
        _io = io;
        _repo = expressionsRepository;
        _appController = appController;
    }

    public void save(ByteBeatExpression expression) throws IOException {
        _io.save(expression);
    }

    public void delete(ByteBeatExpression expression) throws IOException {
        _repo.remove(expression);
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
