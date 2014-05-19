package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.views.IBufferView;

public class BufferVisualsPresenter {
    private final ParametersPresenter _paramaters;
    private IBufferView _bufferView;
    private boolean _die;
    private final IExpressionEvaluator _evaluator;
    private byte[] _samples;

    public BufferVisualsPresenter(
            ParametersPresenter parametersPresenter,
            IExpressionEvaluator evaluator) {
        _die = false;
        _evaluator = evaluator;
        _paramaters = parametersPresenter;
    }

    public void setBuffer(byte[] samples){
        _samples = samples;
    }

    public void setView(IBufferView view){
        _bufferView = view;
    }

    public void start() {
        _die = false;
        _bufferView.setDisplayBuffer(_samples);
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (_die) return;
                    _bufferView.update();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }).start();
    }

    public void stop() {
        _die = true;
    }

    public void resetT() {
        _evaluator.resetTime();
    }

    public void resetArgs() {
        _paramaters.OnResetParameters();
    }
}

