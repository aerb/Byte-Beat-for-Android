package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.views.IKeyboardDisplayView;
import com.tasty.fish.views.IExpressionView;

public class ExpressionPresenter implements
        IKeyboardDisplayView.IKeyboardDisplayViewListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{
    private IExpressionView _view;
    private ByteBeatExpression _expression;

    private String _text;
    private int _cursor = 0;
    private IKeyboardDisplayView _keyboardView;
    private final IExpressionsRepository _expressionRepo;

    public ExpressionPresenter(IExpressionsRepository expressionsRepository) {
        _expressionRepo = expressionsRepository;
        _expressionRepo.setIExpressionsRepositoryListener(this);
        setActiveExpression(_expressionRepo.getActive());
    }

    private void setActiveExpression(ByteBeatExpression exp){
        _expression = exp;
        _text = _expression.getExpressionString() + " ";
        _cursor = _text.length() - 1;
    }

    public void setExpressionView(IExpressionView view) {
        _view = view;
        _view.setExpression(_text, _cursor);
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
        updateView();
    }

    @Override
    public void OnAddElement(String element) {
        _text = _text.substring(0, _cursor) + element
                + _text.substring(_cursor);
        advanceCursor(element.length());
        updateView();
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
}
