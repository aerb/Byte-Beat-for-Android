package com.tasty.fish.views;

public interface IExpressionView {
    void indicateError(boolean b);

    interface IExpressionViewListener
    {
        void OnRequestEdit();
    }

    void setIExpressionViewListener(IExpressionViewListener listener);
    void setExpression(String s, int cursor);
}
