package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IKeyboardDisplayView;
import com.tasty.fish.views.IExpressionView;

public class ExpressionPresenter implements
        IKeyboardDisplayView.IKeyboardDisplayViewListener,
        IExpressionsRepository.IExpressionsRepositoryListener, IExpressionView.IExpressionViewListener {
    private IExpressionView _view;
    private ByteBeatExpression _expression;

    private String _text;
    private int _cursor = 0;
    private IKeyboardDisplayView _keyboardView;
    private final IExpressionsRepository _expressionRepo;
    private final IAppController _appController;
    private final IExpressionEvaluator _evaluator;

    public ExpressionPresenter(
            IExpressionEvaluator evaluator,
            IExpressionsRepository expressionsRepository,
            IAppController appController)
    {
        _evaluator = evaluator;
        _expressionRepo = expressionsRepository;
        _expressionRepo.setIExpressionsRepositoryListener(this);
        setActiveExpression(_expressionRepo.getActive());

        _appController = appController;
    }

    private void setActiveExpression(ByteBeatExpression exp){
        _expression = exp;
        _text = _expression.getExpressionString() + " ";
        _cursor = _text.length() - 1;
    }

    public void setView(IExpressionView view) {
        _view = view;
        _view.setExpression(_text, _cursor);
        _view.setIExpressionViewListener(this);
    }

    public void setKeyboardView(IKeyboardDisplayView keyboardView) {
        _keyboardView = keyboardView;
        _keyboardView.registerIKeyboardDisplayViewListener(this);
    }

    //region IKeyboardDisplayViewListener
    @Override
    public void OnMoveLeft() {
        advanceCursor(-1);
        updateView();
    }

    @Override
    public void OnMoveRight() {
        advanceCursor(1);
        updateView();
    }

    @Override
    public void OnDelete() {
        if (_cursor - 1 >= 0) {
            _text = _text.substring(0, _cursor - 1)
                    + _text.substring(_cursor);
            advanceCursor(-1);
        }

        updateDomain();
        updateView();
    }

    private void updateDomain(){
        _expressionRepo.getActive().setExpressionString(_text);

        try {
            _evaluator.updateExpression(_text);
        } catch (ExpressionParsingException e) {
        }
    }

    @Override
    public void OnAddElement(String element) {
        _text = _text.substring(0, _cursor) + element
                + _text.substring(_cursor);
        advanceCursor(element.length());

        updateDomain();
        updateView();
    }

    @Override
    public void OnCloseKeyboard() {
        _appController.CloseKeyboard();
    }
    //endregion

    private void advanceCursor(int spaces) {
        _cursor = (_cursor + spaces < _text.length() && _cursor + spaces >= 0) ? _cursor
                + spaces
                : _cursor;
    }

    private void updateView(){
        _view.setExpression(_text,_cursor);
    }

    @Override
    public void OnActiveExpressionChanged() {
        setActiveExpression(_expressionRepo.getActive());
        updateView();
    }

    @Override
    public void OnRequestEdit() {
        _appController.ShowKeyboard();
    }
}
