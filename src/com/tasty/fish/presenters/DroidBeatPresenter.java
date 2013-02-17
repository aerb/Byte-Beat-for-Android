package com.tasty.fish.presenters;

import java.util.ArrayList;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;

public class DroidBeatPresenter implements IDroidBeatView.IDroidBeatViewListener{
    private IDroidBeatView _view;
    private boolean _die;

    private ArrayList<ByteBeatExpression> _expressions = null;
    private ByteBeatExpression _activeExpression = null;

    byte samples[] = new byte[1024];
    private boolean _dieFlag;

    private static String[] s_predefinedTitles = { "bleullama-fun", "harism",
            "tangent128", "miiro", "xpansive", "tejeez" };
    private static String[] s_predefinedExpressions = {
            "((t % (p1 * 777)) | (p3 * t)) & ((0xFF * p2)) - t",
            "(((p1 * t) >> 1 % (p2 * 128)) + 20) * 3 * t >> 14 * t >> (p3 * 18)",
            "t * (((t >> 9) & (p3 * 10)) | (((p2 * t) >> 11) & 24) ^ ((t >> 10) & 15 & ((p1 * t) >> 15)))",
            "(p1 * t) * 5 & ((p2 * t) >> 7) | (p3 * t * 3) & (t * 4 >> 10)",
            "(((p1 * t) * ((p2 * t) >> 8 | t >> 9) & (p3 * 46) & t >> 8)) ^ (t & t >> 13 | t >> 6)",
            "((p1 * t) * ((p2 * t) >> 5 | t >> 8)) >> ((p3 * t) >> 16)" };

    private final IExpressionEvaluator _evaluator;

    public DroidBeatPresenter(IDroidBeatView view, IExpressionEvaluator evaluator) {
        _view = view;
        _evaluator = evaluator;
        _expressions = new ArrayList<ByteBeatExpression>();

        initializeExpressions();
    }

    private void initializeExpressions(){
        for (int i = 0; i < s_predefinedExpressions.length; ++i)
            addNewExpression(s_predefinedTitles[i],
                    s_predefinedExpressions[i]);

        addNewExpression("custom", "t");
    }

    private void stopAudioThread() {
        _die = true;
    }

    private void updateView() {
        _view.updateSeekerSpeedPostion(_activeExpression.getSpeed());
        _view.updateSeekerPostion(0, _activeExpression.getArguement(0));
        _view.updateSeekerPostion(1, _activeExpression.getArguement(1));
        _view.updateSeekerPostion(2, _activeExpression.getArguement(2));
    }

    private void setActiveExpression(int id) {
        _activeExpression = _expressions.get(id);
        updateView();
    }

    private boolean addNewExpression(String title, String exp) {
        ByteBeatExpression e = new ByteBeatExpression(title, exp, 0.5f, 1f, 1f, 1f);
        _expressions.add(e);
        return true;
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

    }

    @Override
    public void OnStartPlay() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void OnStopPlay() {
        if (_dieFlag == false) {
            stopAudioThread();
            _dieFlag = true;
        } else {
            startAudioThread();
            startVideoThread();
            _dieFlag = false;
        }
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
