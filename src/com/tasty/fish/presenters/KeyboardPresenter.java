package com.tasty.fish.presenters;

import com.tasty.fish.R;
import com.tasty.fish.R.id;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IKeyboardDisplayView;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class KeyboardPresenter implements OnClickListener {
    private IKeyboardDisplayView _view;
    private String _text;
    private ByteBeatExpression _e;

    private int _cursor = 0;

    public KeyboardPresenter(IKeyboardDisplayView view) {
        _view = view;
        registerButtonListeners(_view.getInflatedKeyboard());
    }

    public void registerButtonListeners(View view) {
        if (view instanceof Button) {
            ((Button) view).setOnClickListener(this);
            return;
        }

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
            View v = ((ViewGroup) view).getChildAt(i);
            registerButtonListeners(v);
        }
    }

    public void setEditableExpression(ByteBeatExpression e) {
        _e = e;
        _text = e.expressionString() + " ";
        _cursor = _text.length() - 1;
        _view.updateDisplayedExpression(_text, _cursor);
    }

    private void advanceCursor(int spaces) {
        _cursor = (_cursor + spaces < _text.length() && _cursor + spaces >= 0) ? _cursor
                + spaces
                : _cursor;
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof Button))
            return;
        Button b = (Button) v;
        int bid = b.getId();
        String btex = (String) b.getText();

        if (bid != R.id.buttonLeft && bid != R.id.buttonRight
                && bid != R.id.buttonDel) {
            _text = _text.substring(0, _cursor) + btex
                    + _text.substring(_cursor);
            advanceCursor(btex.length());
        } else {
            switch (bid) {
            case R.id.buttonLeft:
                advanceCursor(-1);
                break;
            case R.id.buttonRight:
                advanceCursor(1);
                break;
            case R.id.buttonDel:
                _text = _text.substring(0, _cursor - 1)
                        + _text.substring(_cursor);
                advanceCursor(-1);
                break;
            }
        }
        _view.updateDisplayedExpression(_text, _cursor);
        _e.updateExpression(_text);
    }
}
