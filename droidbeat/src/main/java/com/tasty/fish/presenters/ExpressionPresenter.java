package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionListener;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.text.TextCursor;
import com.tasty.fish.text.TextEditor;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IExpressionView;

public class ExpressionPresenter implements
        IExpressionListener,
        IExpressionView.IExpressionViewListener {
    private IExpressionView _view;
    private ByteBeatExpression _expression;

    private TextEditor _editor;
    private TextCursor _cursor;

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
        _expressionRepo.setActiveChangedListener(this);
        _appController = appController;

        _editor = new TextEditor(new TextCursor());
        _cursor = _editor.getCursor();

        setActiveExpression(_expressionRepo.getActive());
    }

    private void setActiveExpression(ByteBeatExpression exp){
        _expression = exp;
        String text = _expression.getExpressionString();
        System.out.println("text --> " + text);
        System.out.println("editor --> " + _editor);
        _editor.setText(text);
    }

    public void setView(IExpressionView view) {
        _view = view;
        _view.setExpression(_editor.getText(), _cursor.getPosition());
        _view.setIExpressionViewListener(this);
    }

    public void moveCursorLeft() {
        _cursor.moveLeft();
        updateView();
    }

    public void moveCursorRight() {
        _cursor.moveRight();
        updateView();
    }

    public void deleteCharacter() {
        _editor.backspace();

        updateDomain();
        updateView();
    }

    private void updateDomain(){
        try {
            _expressionRepo.updateActive(_editor.getText());
            _evaluator.tryParse(_editor.getText());
            _view.indicateError(false);
        } catch (ExpressionParsingException e) {
            _view.indicateError(true);
        }
    }

    public void addElement(String element) {
        element = element.trim();
        _editor.add(element);
        updateDomain();
        updateView();
    }

    public void endEdit() {
        _appController.CloseKeyboard();
    }

    private void updateView(){
        _view.setExpression(_editor.getText(),_cursor.getPosition());
    }

    @Override
    public void onExpressionEvent() {
        setActiveExpression(_expressionRepo.getActive());
        updateView();
    }

    @Override
    public void OnRequestEdit() {
        _appController.ShowKeyboard();
    }
}
