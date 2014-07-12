package com.tasty.fish.android.fragments.keyboard;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.R;
import com.tasty.fish.presenters.ExpressionPresenter;

public class KeyboardFragment extends Fragment implements View.OnClickListener
{
    private ExpressionPresenter _presenter;
    private View _cancelBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _presenter = ((DroidBeatActivity)getActivity())
                .getCompositionRoot()
                .getExpressionPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View keyboardView = inflater.inflate(R.layout.keyboard, null);

        _cancelBtn = keyboardView.findViewById(R.id.keyboardCancelButton);
        _cancelBtn.setOnClickListener(this);

        registerButtonListeners(keyboardView.findViewById(R.id.keyboardLayout));
        return keyboardView;
    }

    public void registerButtonListeners(View view) {
        if (view instanceof Button) {
            Button b = ((Button) view);
            b.setOnClickListener(this);
            if ("0123456789".indexOf((String) b.getText()) >= 0)
                b.getBackground().setColorFilter(0xFFCCCC00, PorterDuff.Mode.MULTIPLY);
            else if ("%/*<<>>+-&^|=!".indexOf((String) b.getText()) >= 0)
                b.getBackground().setColorFilter(0xFFFF5555, PorterDuff.Mode.MULTIPLY);
            return;
        }

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
            View v = ((ViewGroup) view).getChildAt(i);
            registerButtonListeners(v);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == _cancelBtn) {
            _presenter.endEdit();
            return;
        }

        if (!(v instanceof Button))
            return;

        Button b = (Button) v;
        int bid = b.getId();

        if (bid == R.id.buttonLeft) {
            _presenter.moveCursorLeft();
        } else if (bid == R.id.buttonRight) {
           _presenter.moveCursorRight();
        } else if (bid == R.id.buttonDel) {
            _presenter.deleteCharacter();
        } else {
            _presenter.addElement((String) b.getText());
        }
    }
}
