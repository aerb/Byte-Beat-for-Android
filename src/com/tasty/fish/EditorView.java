package com.tasty.fish;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IKeyboardDisplayView;

public class EditorView extends Fragment implements View.OnClickListener, IKeyboardDisplayView {

    private String _text;
    private ByteBeatExpression _e;
    private int _cursor = 0;

    private UnderlineSpan m_underlineSpan = new UnderlineSpan();

    public void setExpression(String s, int cursor) {
        SpannableString content = new SpannableString(s);
        content.removeSpan(m_underlineSpan);
        content.setSpan(m_underlineSpan, cursor, cursor + 1, 0);
        //m_textExpressionView.setText(content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View keyboardView = inflater.inflate(R.layout.keyboard, null);

        registerButtonListeners(keyboardView);
        return keyboardView;
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
        setExpression(_text, _cursor);
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

        setExpression(_text, _cursor);
        _e.setExpression(_text);
    }
}
