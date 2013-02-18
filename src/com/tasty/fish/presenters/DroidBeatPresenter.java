package com.tasty.fish.presenters;

import java.util.ArrayList;

import com.tasty.fish.IParameterView;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;

public class DroidBeatPresenter implements IDroidBeatView.IDroidBeatViewListener, IParameterView.IParameterViewListener {
    private IDroidBeatView _view;
    private boolean _die;

    private ArrayList<ByteBeatExpression> _expressions = null;
    private ByteBeatExpression _activeExpression = null;

    byte samples[] = new byte[1024];
    private boolean _dieFlag;

    private final IExpressionEvaluator _evaluator;
    private IParameterView _paramView;

    public DroidBeatPresenter(IDroidBeatView view, IExpressionEvaluator evaluator) {
        _view = view;
        _evaluator = evaluator;
        _expressions = new ArrayList<ByteBeatExpression>();
    }

    public void setParameterView(IParameterView view){
        _paramView = view;
        _paramView.registerIDroidBeatViewListener(this);
    }

    private void stopAudioThread() {
        _die = true;
    }

    private void updateView() {
        if(_paramView == null) return;

        _paramView.updateSeekerSpeedPostion(_activeExpression.getSpeed());
        _paramView.updateSeekerPostion(0, _activeExpression.getArguement(0));
        _paramView.updateSeekerPostion(1, _activeExpression.getArguement(1));
        _paramView.updateSeekerPostion(2, _activeExpression.getArguement(2));
    }

    private void setActiveExpression(int id) {
        _activeExpression = _expressions.get(id);
        updateView();
    }

    private void startVideoThread() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    _view.setDisplayBuffer(samples, _evaluator.getTime());
                    _view.setTime(_evaluator.getTime());
                    if (_die) {
                        return;
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    private void startAudioThread() {
        _die = false;
        new Thread(new Runnable() {
            public void run() {
                AndroidAudioDevice audio = new AndroidAudioDevice();

                while (true) {

                    for (int i = 0; i < samples.length; i++)
                        samples[i] = _evaluator.getNextSample();

                    audio.writeSamples(samples);

                    if (_die) {
                        audio.stop();
                        return;
                    }
                }
            }
        }).start();
    }

    private void updateTimeScale(double inc) {
        _activeExpression.setTimeScale(inc);
    }

    private void updateArgument(int i, double x) {
        _activeExpression.setArguement(i, x);
    }

    private void resetTime() {
        _evaluator.setTime(0);
    }

    private void resetArgs() {
        _activeExpression.setTimeScale(0.5f);
        _activeExpression.setArguement(0, 1f);
        _activeExpression.setArguement(1, 1f);
        _activeExpression.setArguement(2, 1f);
        updateView();
    }

    public ArrayList<ByteBeatExpression> getExpressions() {
        return _expressions;
    }

    //region IDroidBeatViewListener methods
    @Override
    public void OnExpressionChanged(int id) {
        setActiveExpression(id);
    }

    @Override
    public void OnStartPlay() {
        startAudioThread();
    }

    @Override
    public void OnStopPlay() {
        stopAudioThread();
    }

    @Override
    public void OnArgumentChanged(int index, double value) {
        updateArgument(index, value);
    }

    @Override
    public void OnTimeScaleChanged(double value) {
        updateTimeScale(value);
    }

    @Override
    public void OnResetArgs() {
        resetArgs();
    }

    @Override
    public void OnResetTime() {
        resetTime();
    }
    //endregion
}
