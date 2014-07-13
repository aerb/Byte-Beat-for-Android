package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.views.IAppController;

import java.io.IOException;

public class ExpressionSelectionPresenter
{
    private final ExpressionIO _io;
    private IExpressionList _repo;
    private final IAppController _appController;

    public ExpressionSelectionPresenter(
        ExpressionIO io,
        IExpressionList expressionsRepository,
        IAppController appController) {
        _io = io;
        _repo = expressionsRepository;
        _appController = appController;
    }

    public void save(Expression expression) throws IOException {
        _io.save(expression);
    }

    public void delete(Expression expression) throws IOException {
        _repo.remove(expression);
        _io.delete(expression);
    }

    public void selectExpression(Expression expression) {
        _repo.setActive(expression);
    }

    public void select(int index){
        _repo.setActive(index);
    }

    public void exitView() {
        _appController.CloseSelector();
    }
}
