package com.tasty.fish.presenters;

import java.io.IOException;

public class ExpressionLoadingException extends IOException {
    private final String _expressionFailed;

    public ExpressionLoadingException(String expressionName) {
        _expressionFailed = expressionName;
    }

    public String getFailure() {
        return _expressionFailed;
    }
}
