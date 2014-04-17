package com.tasty.fish.presenters;
import android.text.Editable;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.views.ICreateExpressionView;

public class CreateExpressionPresenter {

    private ICreateExpressionView _view;
    private final IExpressionsRepository _repo;

    public CreateExpressionPresenter(
            IExpressionsRepository repo
        )
    {
        _repo = repo;
    }

    public void setView(ICreateExpressionView view) {
        _view = view;
    }

    public void requestDefaultName(){
        String defaultName = "new_expression";
        _view.setDefaultName(defaultName);
    }

    public void addNewExpression(String text, String copy) {
        _repo.addNewExpression(new ByteBeatExpression(
                text,
                copy != null ? copy : "t",
                0.5,
                50,
                50,
                50
        ));
        _repo.setActiveExpressionLast();
    }
}
