package com.tasty.fish.android.fragments.parameters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.R;
import com.tasty.fish.presenters.ParametersPresenter;
import com.tasty.fish.views.IParameterView;

import java.util.ArrayList;

public class ParametersFragment extends Fragment implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        IParameterView
{
    private static SeekBar s_seekBarSpeed;
    private static SeekBar[] s_seekBarArgs = new SeekBar[3];

    private static TextView s_textSpeed;
    private static TextView[] s_textArgs = new TextView[3];

    private static Button s_resetTimeBtn;
    private static Button s_resetArgsBtn;

    private ArrayList<IParameterViewListener> _listeners;
    private ParametersPresenter _presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parameterView = inflater.inflate(R.layout.params, null);
        _listeners = new ArrayList<IParameterViewListener>();

        s_textSpeed = (TextView) parameterView.findViewById(R.id.textSpeed);
        s_textArgs[0] = (TextView) parameterView.findViewById(R.id.textArg1);
        s_textArgs[1] = (TextView) parameterView.findViewById(R.id.textArg2);
        s_textArgs[2] = (TextView) parameterView.findViewById(R.id.textArg3);
        s_seekBarSpeed = (SeekBar) parameterView.findViewById(R.id.seekBarSpeed);
        s_seekBarArgs[0] = (SeekBar) parameterView.findViewById(R.id.seekBarArg1);
        s_seekBarArgs[1] = (SeekBar) parameterView.findViewById(R.id.seekBarArg2);
        s_seekBarArgs[2] = (SeekBar) parameterView.findViewById(R.id.seekBarArg3);
        s_resetArgsBtn = (Button) parameterView.findViewById(R.id.buttonResetArgs);
        s_resetTimeBtn = (Button) parameterView.findViewById(R.id.buttonResetTime);

        s_resetTimeBtn.setOnClickListener(this);
        s_resetArgsBtn.setOnClickListener(this);
        s_seekBarSpeed.setOnSeekBarChangeListener(this);
        s_seekBarArgs[0].setOnSeekBarChangeListener(this);
        s_seekBarArgs[1].setOnSeekBarChangeListener(this);
        s_seekBarArgs[2].setOnSeekBarChangeListener(this);

        _presenter =
            ((DroidBeatActivity)getActivity())
            .getCompositionRoot()
            .getParametersPresenter();

        _presenter.setView(this);
        return parameterView;
    }

    //region Conversion methods
    private double convertSeekBarToParameterValue(double arg1) {
        double x = ((float) arg1) / 100 * 2;
        x = (float) (x == 0 ? 0.01 : x);
        return x;
    }

    private double convertSeekBarToTimescaleValue(double arg1) {
        return arg1 / 100;
    }
    //endregion

    //region OnClickListener methods
    @Override
    public void onClick(View arg0) {
        if (arg0 == s_resetTimeBtn) {
            NotifyResetTime();
        } else if (arg0 == s_resetArgsBtn) {
            NotifyResetParameters();
        }
    }
    //endregion

    //region Notification methods
    private void NotifyResetParameters() {
        for(IParameterViewListener listener : _listeners){
            listener.OnResetParameters();
        }
    }

    private void NotifyResetTime() {
        for(IParameterViewListener listener : _listeners){
            listener.OnResetTime();
        }
    }

    private void NotifyTimeScaleChanged(double value){
        for(IParameterViewListener listener : _listeners){
            listener.OnTimeScaleChanged(value);
        }
    }

    private void NotifyParameterChanged(int index, double value){
        for(IParameterViewListener listener : _listeners){
            listener.OnParameterChanged(index, value);
        }
    }
    //endregion

    //region OnSeekBarChangeListeners methods
    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        if (arg0 == s_seekBarSpeed) {
            double inc = convertSeekBarToTimescaleValue(arg1);
            NotifyTimeScaleChanged(inc);
            s_textSpeed.setText("speed = " + inc);
        } else {
            double x = convertSeekBarToParameterValue(arg1);
            if (arg0 == s_seekBarArgs[0]) {
                NotifyParameterChanged(0, x);
                s_textArgs[0].setText(String.format("p1 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[1]) {
                NotifyParameterChanged(1, x);
                s_textArgs[1].setText(String.format("p2 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[2]) {
                NotifyParameterChanged(2, x);
                s_textArgs[2].setText(String.format("p3 = %.2f", x));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {}

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {}
    //endregion

    //region IParameterView methods
    @Override
    public void registerIDroidBeatViewListener(IParameterViewListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setTimescale(double value) {
        s_seekBarSpeed.setProgress((int) (value * 100));
    }

    @Override
    public void setParameter(int i, double value) {
        s_seekBarArgs[i].setProgress((int) (value * 100 / 2));
    }
    //endregion
}
