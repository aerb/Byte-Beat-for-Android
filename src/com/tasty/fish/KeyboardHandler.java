package com.tasty.fish;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class KeyboardHandler implements OnClickListener {
    View _view;
    TextView _text;
    UnderlineSpan ul = new UnderlineSpan();
    int _cursor = 0;

    public KeyboardHandler(TextView text, View view) {
        _view = view;
        _text = text;
        registerButtonListeners(_view);
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

    @Override
    public void onClick(View v) {
        if (!(v instanceof Button))
            return;
        Button b = (Button) v;
        int bid = b.getId();
        CharSequence curr = (CharSequence) _text.getText();
        SpannableString content;
        if (bid != R.id.buttonLeft && bid != R.id.buttonRight)
            content = new SpannableString(TextUtils.concat(curr, b.getText(),
                    " "));
        else
            content = new SpannableString(curr);

        switch (b.getId()) {
        case R.id.buttonLeft:
            _cursor = (_cursor - 1 < 0) ? _cursor : _cursor - 1;
            break;
        case R.id.buttonRight:
            _cursor = (_cursor + 1 >= content.length()) ? _cursor : _cursor + 1;
            break;
        }

        content.removeSpan(ul);
        content.setSpan(ul, _cursor, _cursor + 1, 0);

        _text.setText(content);
    }
}
