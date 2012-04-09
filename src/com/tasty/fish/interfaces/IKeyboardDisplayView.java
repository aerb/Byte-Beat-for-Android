package com.tasty.fish.interfaces;

import android.view.View;

public interface IKeyboardDisplayView {
    public View getInflatedKeyboard();
    public void updateDisplayedExpression(String s, int cursor);
}
