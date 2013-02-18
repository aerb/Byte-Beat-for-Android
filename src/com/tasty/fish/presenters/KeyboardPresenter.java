package com.tasty.fish.presenters;

import com.tasty.fish.R;

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IKeyboardDisplayView;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.PorterDuff;

public class KeyboardPresenter {
    private IKeyboardDisplayView _view;

    public KeyboardPresenter(IKeyboardDisplayView view) {
        _view = view;
    }


}
