package com.tasty.fish.presenters;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.utils.NamingResponse;
import com.tasty.fish.utils.NamingRules;

public class ExpressionBuilder {

    private final IExpressionList _repo;

    public ExpressionBuilder(
            IExpressionList repo
    )
    {
        _repo = repo;
    }

    public Expression addNewExpression(String text, String expression) {
        Expression exp = new Expression(
                text,
                expression,
                false
        );
        exp.setExpressionString(expression);
        _repo.add(exp);
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

    public NamingResponse isValidName(String name) {
        if(_repo.contains(name)) return NamingResponse.AlreadyExists;
        if(!NamingRules.check(name)) return NamingResponse.InvalidChar;
        return NamingResponse.Valid;
    }
}
