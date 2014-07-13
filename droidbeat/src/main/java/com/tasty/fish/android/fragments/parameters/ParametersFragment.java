package com.tasty.fish.android.fragments.parameters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;
import com.tasty.fish.domain.implementation.Expression;

import java.security.InvalidParameterException;

public class ParametersFragment extends Fragment implements
        SeekBar.OnSeekBarChangeListener
{
    private SeekBar _seekBarSpeed;
    private SeekBar[] _seekBarArgs = new SeekBar[3];

    private TextView _textSpeed;
    private TextView[] _textArgs = new TextView[3];

    private IExpressionList _repo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.params, null);
        if (view == null) throw new InvalidParameterException();

        _textSpeed = (TextView) view.findViewById(R.id.textSpeed);
        _textArgs[0] = (TextView) view.findViewById(R.id.textArg1);
        _textArgs[1] = (TextView) view.findViewById(R.id.textArg2);
        _textArgs[2] = (TextView) view.findViewById(R.id.textArg3);
        _seekBarSpeed = (SeekBar) view.findViewById(R.id.seekBarSpeed);
        _seekBarArgs[0] = (SeekBar) view.findViewById(R.id.seekBarArg1);
        _seekBarArgs[1] = (SeekBar) view.findViewById(R.id.seekBarArg2);
        _seekBarArgs[2] = (SeekBar) view.findViewById(R.id.seekBarArg3);

        _seekBarSpeed.setOnSeekBarChangeListener(this);
        _seekBarArgs[0].setOnSeekBarChangeListener(this);
        _seekBarArgs[1].setOnSeekBarChangeListener(this);
        _seekBarArgs[2].setOnSeekBarChangeListener(this);

        _repo =
            ((DroidBeatActivity)getActivity())
            .getCompositionRoot()
            .getExpressionsRepository();

        Listener<Expression> updates = new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
                updateView(item);
            }
        };

        _repo.addExpressionUpdateListener(updates);
        _repo.addActiveChangedListener(updates);

        updateView(_repo.getActive());

        return view;
    }

    private void updateView(Expression item){
        _seekBarSpeed.setProgress((int) ((item.getTimeDelta()/2) * 100));
        _seekBarArgs[0].setProgress(item.getArgument(0));
        _seekBarArgs[1].setProgress(item.getArgument(1));
        _seekBarArgs[2].setProgress(item.getArgument(2));
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        Expression expression = _repo.getActive();
        if (arg0 == _seekBarSpeed) {
            double inc = ((double)arg1*2)/100;
            expression.setTimeDelta(inc);
            _textSpeed.setText(String.format("%.2f", inc));
        } else {
            if (arg0 == _seekBarArgs[0]) {
                expression.setParameter(0, arg1);
                _textArgs[0].setText(String.format("%d", arg1));
            } else if (arg0 == _seekBarArgs[1]) {
                expression.setParameter(1, arg1);
                _textArgs[1].setText(String.format("%d", arg1));
            } else if (arg0 == _seekBarArgs[2]) {
                expression.setParameter(2, arg1);
                _textArgs[2].setText(String.format("%d", arg1));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {}

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {}
}
