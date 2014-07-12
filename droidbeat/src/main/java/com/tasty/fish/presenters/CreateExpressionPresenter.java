package com.tasty.fish.presenters;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;

import java.util.regex.Pattern;

public class CreateExpressionPresenter {

    private final IExpressionsRepository _repo;

    public CreateExpressionPresenter(
            IExpressionsRepository repo
        )
    {
        _repo = repo;
    }

    public ByteBeatExpression addNewExpression(String text, String expression) {
        ByteBeatExpression exp = new ByteBeatExpression(
                text,
                expression,
                0.5,
                50,
                50,
                50,
                false
        );
        exp.setExpressionString(expression);
        _repo.addNewExpression(exp);
        return exp;
    }

    public String getSuggestedName() {
        String nameBase = "new_expression_";
        int i = 0;
        String name;
        do {
            name = nameBase + i++;
        } while (_repo.contains(name));
        return name;
    }

    private Pattern validNames = Pattern.compile("[a-zA-Z0-9_]+");

    public NamingResponse isValidName(String name) {
        if(_repo.contains(name)) return NamingResponse.AlreadyExists;
        if(!validNames.matcher(name).matches()) return NamingResponse.InvalidChar;
        return NamingResponse.Valid;
    }
}
