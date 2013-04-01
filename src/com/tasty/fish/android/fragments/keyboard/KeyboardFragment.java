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
import com.tasty.fish.views.IKeyboardDisplayView;

import java.util.ArrayList;

public class KeyboardFragment extends Fragment implements
        View.OnClickListener,
        IKeyboardDisplayView
{
    private ExpressionPresenter _presenter;
    private ArrayList<IKeyboardDisplayViewListener> _listeners;
    private View _cancelBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _listeners = new ArrayList<IKeyboardDisplayViewListener>();

        _presenter = ((DroidBeatActivity)getActivity())
                .getCompositionRoot()
                .getExpressionPresenter();

        _presenter.setKeyboardView(this);
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

    @Override
    public void onClick(View v) {
        if(v == _cancelBtn){
            for(IKeyboardDisplayViewListener l : _listeners)
                l.OnCloseKeyboard();
            return;
        }

        if (!(v instanceof Button))
            return;
        Button b = (Button) v;
        int bid = b.getId();
        String btnText = (String) b.getText();

        if (bid != R.id.buttonLeft &&
            bid != R.id.buttonRight &&
            bid != R.id.buttonDel)
        {
            for(IKeyboardDisplayViewListener l : _listeners)
                l.OnAddElement(btnText);
        } else {
            switch (bid) {
                case R.id.buttonLeft:
                    for(IKeyboardDisplayViewListener l : _listeners)
                        l.OnMoveLeft();
                    break;
                case R.id.buttonRight:
                    for(IKeyboardDisplayViewListener l : _listeners)
                        l.OnMoveRight();
                    break;
                case R.id.buttonDel:
                    for(IKeyboardDisplayViewListener l : _listeners)
                        l.OnDelete();
                    break;
            }
        }
    }

    @Override
    public void registerIKeyboardDisplayViewListener(IKeyboardDisplayViewListener listener) {
        _listeners.add(listener);
    }
}
