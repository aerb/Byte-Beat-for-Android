package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.views.IBufferView;

public class BufferVisualsPresenter {
    private IBufferView _bufferView;
    private boolean _die;
    private final IExpressionEvaluator _evaluator;
    private byte[] _samples;

    public BufferVisualsPresenter(IExpressionEvaluator evaluator) {
        _die = false;
        _evaluator = evaluator;
    }

    public void setSamples(byte[] samples){
        _samples = samples;
    }

    public void setBufferView(IBufferView view){
        _bufferView = view;
    }

    private void startVideo() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    _bufferView.setDisplayBuffer(_samples, _evaluator.getTime());
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
}

