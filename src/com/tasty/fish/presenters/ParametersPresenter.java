package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.ExpressionParsingException;
import com.tasty.fish.views.IParameterView;

public class ParametersPresenter implements
        IParameterView.IParameterViewListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{

    private IParameterView _parametersView;
    private final IExpressionsRepository _repo;
    private ByteBeatExpression _activeExpression;
    private final IExpressionEvaluator _evaluator;

    public ParametersPresenter(
        IExpressionsRepository repo,
        IExpressionEvaluator evaluator)
    {
        _repo = repo;
        _evaluator = evaluator;
    }

    public void setParameterView(IParameterView view){
        _parametersView = view;
        _parametersView.registerIDroidBeatViewListener(this);
    }

    private void updateView(){
        if(_parametersView == null) return;

        _parametersView.setTimescale(_activeExpression.getSpeed());
        _parametersView.setParameter(0, _activeExpression.getArguement(0));
        _parametersView.setParameter(1, _activeExpression.getArguement(1));
        _parametersView.setParameter(2, _activeExpression.getArguement(2));
    }

    private void updateTimeScale(double inc) {
        _activeExpression.setTimescale(inc);
    }

    private void updateArgument(int i, double x) {
        _activeExpression.setParameter(i, x);
    }

    private void resetTime() {
        _evaluator.resetTime();
    }

    private void resetArgs() {
        _activeExpression.resetParametersAndTimescale();
        updateView();
    }

    //region IParameterViewListener methods
    @Override
    public void OnParameterChanged(int index, double value) {
        updateArgument(index, value);
    }

    @Override
    public void OnTimeScaleChanged(double value) {
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
        ByteBeatExpression exp = _repo.getActive();
        _activeExpression = exp;
        updateView();
    }
    //endregion
}
