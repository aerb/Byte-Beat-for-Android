package com.tasty.fish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tasty.fish.presenters.DroidBeatPresenter;

import java.util.ArrayList;

public class ParametersView extends Fragment implements View.OnClickListener,
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
    private DroidBeatPresenter s_droidbeatPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((DroidBeatView)getActivity()).getDroidbeatPresenter().setParameterView(this);

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

        return parameterView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == s_resetTimeBtn) {
            NotifyResetTime();
        } else if (arg0 == s_resetArgsBtn) {
            NotifyResetArgs();
        }
    }

    private void NotifyResetArgs() {
        for(IParameterViewListener listener : _listeners){
            listener.OnResetArgs();
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

    private void NotifyArguementChanged(int index, double value){
        for(IParameterViewListener listener : _listeners){
            listener.OnArgumentChanged(index, value);
        }
    }

    private double mapArgSeekBar(double arg1) {
        double x = ((float) arg1) / 100 * 2;
        x = (float) (x == 0 ? 0.01 : x);
        return x;
    }

    private double mapTimeSeekBar(double arg1) {
        return arg1 / 100;
    }

    //region OnSeekBarChangeListeners methods
    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        if (arg0 == s_seekBarSpeed) {
            double inc = mapTimeSeekBar(arg1);
            NotifyTimeScaleChanged(inc);
            s_textSpeed.setText("speed = " + inc);
        } else {
            double x = mapArgSeekBar(arg1);
            if (arg0 == s_seekBarArgs[0]) {
                NotifyArguementChanged(0, x);
                s_textArgs[0].setText(String.format("p1 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[1]) {
                NotifyArguementChanged(1, x);
                s_textArgs[1].setText(String.format("p2 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[2]) {
                NotifyArguementChanged(2, x);
                s_textArgs[2].setText(String.format("p3 = %.2f", x));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {}

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {}
    //endregion

    @Override
    public void registerIDroidBeatViewListener(IParameterViewListener listener) {
        _listeners.add(listener);
    }

    public void updateSeekerSpeedPostion(double value) {
        s_seekBarSpeed.setProgress((int) (value * 100));
    }

    public void updateSeekerPostion(int i, double value) {
        s_seekBarArgs[i].setProgress((int) (value * 100 / 2));
    }
}
