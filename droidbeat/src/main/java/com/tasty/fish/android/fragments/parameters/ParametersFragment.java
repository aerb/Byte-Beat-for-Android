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
    private SeekBar _seekBarSpeed;
    private SeekBar[] _seekBarArgs = new SeekBar[3];

    private TextView _textSpeed;
    private TextView[] _textArgs = new TextView[3];

    private Button s_resetTimeBtn;
    private Button s_resetArgsBtn;

    private ArrayList<IParameterViewListener> _listeners;
    private ParametersPresenter _presenter;
    private View _view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.params, null);
        _listeners = new ArrayList<IParameterViewListener>();

        _textSpeed = (TextView) _view.findViewById(R.id.textSpeed);
        _textArgs[0] = (TextView) _view.findViewById(R.id.textArg1);
        _textArgs[1] = (TextView) _view.findViewById(R.id.textArg2);
        _textArgs[2] = (TextView) _view.findViewById(R.id.textArg3);
        _seekBarSpeed = (SeekBar) _view.findViewById(R.id.seekBarSpeed);
        _seekBarArgs[0] = (SeekBar) _view.findViewById(R.id.seekBarArg1);
        _seekBarArgs[1] = (SeekBar) _view.findViewById(R.id.seekBarArg2);
        _seekBarArgs[2] = (SeekBar) _view.findViewById(R.id.seekBarArg3);

        _seekBarSpeed.setOnSeekBarChangeListener(this);
        _seekBarArgs[0].setOnSeekBarChangeListener(this);
        _seekBarArgs[1].setOnSeekBarChangeListener(this);
        _seekBarArgs[2].setOnSeekBarChangeListener(this);

        _presenter =
            ((DroidBeatActivity)getActivity())
            .getCompositionRoot()
            .getParametersPresenter();

        _presenter.setView(this);
        return _view;
    }

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

    private void NotifyTimeDeltaChanged(double value){
        for(IParameterViewListener listener : _listeners){
            listener.OnTimeDeltaChanged(value);
        }
    }

    private void NotifyParameterChanged(int index, int value){
        for(IParameterViewListener listener : _listeners){
            listener.OnParameterChanged(index, value);
        }
    }
    //endregion

    //region OnSeekBarChangeListeners methods
    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        if (arg0 == _seekBarSpeed) {
            double inc = ((double)arg1*2)/100;
            NotifyTimeDeltaChanged(inc);
            _textSpeed.setText(String.format("%.2f", inc));
        } else {
            if (arg0 == _seekBarArgs[0]) {
                NotifyParameterChanged(0, arg1);
                _textArgs[0].setText(String.format("%d", arg1));
            } else if (arg0 == _seekBarArgs[1]) {
                NotifyParameterChanged(1, arg1);
                _textArgs[1].setText(String.format("%d", arg1));
            } else if (arg0 == _seekBarArgs[2]) {
                NotifyParameterChanged(2, arg1);
                _textArgs[2].setText(String.format("%d", arg1));
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
    public void registerIParameterViewListener(IParameterViewListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setTimeDelta(double value) {
        _seekBarSpeed.setProgress((int) ((value/2) * 100));
    }

    @Override
    public void setParameter(int i, int value) {
        _seekBarArgs[i].setProgress(value);
    }

    @Override
    public void update() {
        _view.invalidate();
    }
    //endregion
}
