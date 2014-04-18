package com.tasty.fish.views;

import android.view.View;

public interface IKeyboardDisplayView {
    public interface IKeyboardDisplayViewListener
    {
        void OnMoveLeft();
        void OnMoveRight();
        void OnDelete();
        void OnAddElement(String element);
        void OnCloseKeyboard();
    }

    void registerIKeyboardDisplayViewListener(IKeyboardDisplayViewListener listener);
}
