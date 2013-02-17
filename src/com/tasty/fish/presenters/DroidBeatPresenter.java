package com.tasty.fish.presenters;

import java.util.ArrayList;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;
import com.tasty.fish.domain.ByteBeatExpression.CompiledExpression;

public class DroidBeatPresenter implements IDroidBeatView.IDroidBeatViewListener{
    private IDroidBeatView _view;
    private boolean _die;
    private ArrayList<ByteBeatExpression> _exps = null;
    private ByteBeatExpression _exp = null;
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
        _exps = new ArrayList<ByteBeatExpression>();

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
        _view.updateSeekerSpeedPostion(_exp.getSpeed());
        _view.updateSeekerPostion(0, _exp.getArguement(0));
        _view.updateSeekerPostion(1, _exp.getArguement(1));
        _view.updateSeekerPostion(2, _exp.getArguement(2));
    }

    private void setActiveExpression(int id) {
        _exp = _exps.get(id);
        updateView();
    }

    private boolean addNewExpression(String title, String exp) {
        ByteBeatExpression e = new ByteBeatExpression(title, exp, 0.5f, 1f, 1f,
                1f);
        if (!e.tryParse())
            return false;
        _exps.add(e);
        return true;
    }

    private boolean addNewExpression(String title, String exp,
            CompiledExpression cexp) {
        ByteBeatExpression e = new ByteBeatExpression(title, exp, 0.5f, 1f, 1f, 1f, cexp
        );
        if (!e.tryParse())
            return false;
        _exps.add(e);
        return true;
    }

    private void startVideoThread() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    _view.setDisplayBuffer(samples, _exp.getTime());
                    _view.setTime(_exp.getTime());
                    if (_die) {
                        return;
                    }
                    try {
                        Thread.sleep(100);
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
                        samples[i] = _exp.getNext();

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
        _exp.setTimeScale(inc);
    }

    private void updateArgument(int i, double x) {
        _exp.setArguement(i, x);
    }

    private void resetTime() {
        _evaluator.setTime(0);
    }

    private void resetArgs() {
        _exp.setTimeScale(0.5f);
        _exp.setArguement(0, 1f);
        _exp.setArguement(1, 1f);
        _exp.setArguement(2, 1f);
        updateView();
    }

    public ArrayList<ByteBeatExpression> getExpressions() {
        return _exps;
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
