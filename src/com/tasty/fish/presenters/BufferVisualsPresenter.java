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
                        Thread.sleep(30);
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
}

