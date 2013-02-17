package com.tasty.fish.interfaces;

import android.view.View;

public interface IKeyboardDisplayView {
    public View getInflatedKeyboard();
    public void setExpression(String s, int cursor);
}
