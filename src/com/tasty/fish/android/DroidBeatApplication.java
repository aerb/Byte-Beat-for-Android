package com.tasty.fish.android;

import android.app.Application;
import com.tasty.fish.domain.implementation.ExpressionEvaluator;
import com.tasty.fish.domain.implementation.ExpressionsRepository;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;

public class DroidBeatApplication extends Application {

    private ExpressionSelectionPresenter _expressionSelectorPresenter;

    private IExpressionsRepository _expressionsRepository;

    private ExpressionEvaluator _expressionEvaluator;
    @Override
    public void onCreate() {
        super.onCreate();

        _expressionEvaluator = new ExpressionEvaluator();
        _expressionsRepository = new ExpressionsRepository();
        _expressionSelectorPresenter = new ExpressionSelectionPresenter(_expressionsRepository);
    }

    public ExpressionSelectionPresenter getExpressionSelectorPresenter() {
        return _expressionSelectorPresenter;
    }

    public ExpressionEvaluator getExpressionEvaluator() {
        return _expressionEvaluator;
    }

    public IExpressionsRepository getExpressionsRepository() {
        return _expressionsRepository;
    }
}
