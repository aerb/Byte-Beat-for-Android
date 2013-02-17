package com.tasty.fish;

import android.app.Application;

public class DroidBeatApplication extends Application {

    private ExpressionSelectionPresenter _expressionSelectorPresenter;
    private IExpressionsRepository _expressionsRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        _expressionsRepository = new ExpressionsRepository();
        _expressionSelectorPresenter = new ExpressionSelectionPresenter(_expressionsRepository);
    }

    public ExpressionSelectionPresenter getExpressionSelectorPresenter() {
        return _expressionSelectorPresenter;
    }
}
