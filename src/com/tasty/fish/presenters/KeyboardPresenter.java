package com.tasty.fish.presenters;

import com.tasty.fish.R;

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IKeyboardDisplayView;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.PorterDuff;

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
            Button b = ((Button) view);
            b.setOnClickListener(this);
            if ("0123456789".indexOf((String) b.getText()) >= 0)
                b.getBackground().setColorFilter(0xFFCCCC00,
                        PorterDuff.Mode.MULTIPLY);
            else if ("%/*<<>>+-&^|=!".indexOf((String) b.getText()) >= 0)
                b.getBackground().setColorFilter(0xFFFF5555,
                        PorterDuff.Mode.MULTIPLY);

            return;
        }

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
            View v = ((ViewGroup) view).getChildAt(i);
            registerButtonListeners(v);
        }
    }

    public void setEditableExpression(ByteBeatExpression e) {
        _e = e;
        _text = e.getExpression() + " ";
        _cursor = _text.length() - 1;
        _view.setExpression(_text, _cursor);
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
                if (_cursor - 1 >= 0) {
                    _text = _text.substring(0, _cursor - 1)
                            + _text.substring(_cursor);
                    advanceCursor(-1);
                }
                break;
            }
        }
        _view.setExpression(_text, _cursor);
        _e.setExpression(_text);
    }
}
