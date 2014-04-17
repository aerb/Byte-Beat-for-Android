package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.views.IParameterView;

public class ParametersPresenter implements
        IParameterView.IParameterViewListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{

    private IParameterView _view;
    private final IExpressionsRepository _repo;
    private final IExpressionEvaluator _evaluator;

    public ParametersPresenter(
        IExpressionsRepository repo,
        IExpressionEvaluator evaluator)
    {
        _repo = repo;
        _repo.setIExpressionsRepositoryListener(this);
        _evaluator = evaluator;
    }

    public void setView(IParameterView view){
        _view = view;
        _view.registerIParameterViewListener(this);
        updateView();
    }

    private void updateView(){
        if(_view == null) return;

        _view.setTimeDelta(_repo.getActive().getTimeDelta());
        _view.setParameter(0, _repo.getActive().getArgument(0));
        _view.setParameter(1, _repo.getActive().getArgument(1));
        _view.setParameter(2, _repo.getActive().getArgument(2));
        _view.update();
    }

    private void updateTimeScale(double inc) {
        _repo.getActive().setTimeDelta(inc);
        _evaluator.updateTimedelta(inc);
    }

    private void updateArgument(int i, int x) {
        _repo.getActive().setParameter(i, x);
        _evaluator.updateArgument(i, x);
    }

    private void resetTime() {
        _evaluator.resetTime();
    }

    private void resetArgs() {
        _repo.getActive().resetParametersAndTimeDelta();
        updateView();
    }

    //region IParameterViewListener methods
    @Override
    public void OnParameterChanged(int index, int value) {
        updateArgument(index, value);
    }

    @Override
    public void OnTimeDeltaChanged(double value) {
        updateTimeScale(value);
    }

    @Override
    public void OnResetParameters() {
        resetArgs();
    }

    @Override
    public void OnResetTime() {
        resetTime();
    }
    //endregion

    //region IExpressionRepository methods
    @Override
    public void OnActiveExpressionChanged() {
        updateView();
    }
    //endregion
}
