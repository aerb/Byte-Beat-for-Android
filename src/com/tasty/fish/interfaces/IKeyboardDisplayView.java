package com.tasty.fish.interfaces;

import android.view.View;

public interface IKeyboardDisplayView {
    public interface IKeyboardDisplayViewListener
    {
        void OnMoveLeft();
        void OnMoveRight();
        void OnDelete();
        void OnAddElement(String element);
    }

    void registerIKeyboardDisplayViewListener(IKeyboardDisplayViewListener listener);
}
