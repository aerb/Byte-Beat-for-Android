package com.tasty.fish;

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IKeyboardDisplayView;

public class ExpressionPresenter implements IKeyboardDisplayView.IKeyboardDisplayViewListener {
    private IExpressionView _view;
    private final ByteBeatExpression _expression;

    private String _text = "aaaaa";
    private int _cursor = 0;
    private IKeyboardDisplayView _keyboardView;

    public ExpressionPresenter() {
        _expression = new ByteBeatExpression("sgsdg", "sdgsdg",1,1,1,1);
    }

    public void setExpressionView(IExpressionView view) {
        _view = view;

        //String text = _expression.getExpression() + " ";
        //int cursor = text.length() - 1;
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


}
