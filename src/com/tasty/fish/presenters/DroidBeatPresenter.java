package com.tasty.fish.presenters;

import java.util.ArrayList;

import com.tasty.fish.views.IBufferView;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.views.IParameterView;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.views.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;
import com.tasty.fish.utils.parser.ExpressionParsingException;

public class DroidBeatPresenter implements IDroidBeatView.IDroidBeatViewListener,
                                           IParameterView.IParameterViewListener,
                                           IExpressionsRepository.IExpressionsRepositoryListener
{
    private IDroidBeatView _view;
    private boolean _die;

    private ArrayList<ByteBeatExpression> _expressions = null;
    private ByteBeatExpression _activeExpression = null;

    byte samples[];
    private boolean _dieFlag;

    private final IExpressionEvaluator _evaluator;
    private IParameterView _paramView;
    private IBufferView _bufferView;
    private final IExpressionsRepository _repo;
    private final AndroidAudioDevice _audioDevice;

    public DroidBeatPresenter(
            IDroidBeatView view,
            IExpressionEvaluator evaluator,
            IExpressionsRepository repo
    )
    {
        _audioDevice = new AndroidAudioDevice();
        samples = new byte[_audioDevice.getBufferSize()];
        _view = view;
        _evaluator = evaluator;
        _repo = repo;
        _repo.setIExpressionsRepositoryListener(this);
        _expressions = new ArrayList<ByteBeatExpression>();
    }

    public void setBufferView(IBufferView view){
        _bufferView = view;
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
        _view.setTitle(_activeExpression.getName());
    }

    private void startVideoThread() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    _bufferView.setDisplayBuffer(samples, _evaluator.getTime());
                    _bufferView.setTime(_evaluator.getTime());

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
                _audioDevice.play();
                while (true) {
                    for (int i = 0; i < samples.length; i++){
                        samples[i] = _evaluator.getNextSample();
                        //t += 0.1;
                        //samples[i] = (byte)((Math.sin((double)t)+1)*50);
                        //System.out.println(samples[i]);
                    }
                    _audioDevice.writeSamples(samples);

                    if (_die) {
                        _audioDevice.stop();
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
        _evaluator.resetTime();
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
    public void OnStartPlay() {
        startAudioThread();
        //startVideoThread();
    }

    @Override
    public void OnStopPlay() {
        stopAudioThread();
    }
    //endregion

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


    @Override
    public void OnActiveExpressionChanged() {
        ByteBeatExpression exp = _repo.getActive();
        try {
            _evaluator.setExpression(exp);
        } catch (ExpressionParsingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        _activeExpression = exp;
        updateView();
    }
}
