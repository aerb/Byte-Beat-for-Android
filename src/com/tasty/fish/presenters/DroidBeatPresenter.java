package com.tasty.fish.presenters;

import java.util.ArrayList;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;
import com.tasty.fish.domain.ByteBeatExpression.CompiledExpression;

public class DroidBeatPresenter implements IDroidBeatView.IDroidBeatViewListener{
    private IDroidBeatView _view;
    private boolean _die;
    private ArrayList<ByteBeatExpression> _exps = null;
    private ByteBeatExpression _exp = null;
    byte samples[] = new byte[1024];

    public DroidBeatPresenter(IDroidBeatView view) {
        _view = view;
        _exps = new ArrayList<ByteBeatExpression>();
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
                    _view.displayBuffer(samples, _exp.getTime());
                    _view.updateT(_exp.getTime());
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
        _exp.resetTime();
    }

    private void resetArgs() {
        _exp.setTimeScale(0.5f);
        _exp.setArguement(0, 1f);
        _exp.setArguement(1, 1f);
        _exp.setArguement(2, 1f);
        updateView();
    }

    private ArrayList<ByteBeatExpression> getExpressions() {
        return _exps;
    }

    //region IDroidBeatViewListener methods
    @Override
    public void OnExpressionChanged(String newExpression) {

    }

    @Override
    public void OnArgumentChanged(int index, double value) {

    }

    @Override
    public void OnTimeScaleChanged(double value) {

    }
    //endregion
}
