package com.tasty.fish.android.media.audio;


import com.tasty.fish.domain.IExpressionEvaluator;

public class AudioPlayer implements IAudioPlayer {

    byte _samples[];
    private final AndroidAudioDevice _audioDevice;
    private final IExpressionEvaluator _evaluator;
    private boolean _die;

    public AudioPlayer(IExpressionEvaluator evaluator) {
        _audioDevice = new AndroidAudioDevice();
        _samples = new byte[_audioDevice.getBufferSize()];

        _evaluator = evaluator;
        _die = false;
    }

    public byte[] getBuffer(){
        return _samples;
    }

    @Override
    public void start() {
        _die = false;
        new Thread(new Runnable() {
            public void run() {
                _audioDevice.play();
                while (true) {
                    for (int i = 0; i < _samples.length; i++){
                        _samples[i] = _evaluator.getNextSample();
                    }
                    _audioDevice.writeSamples(_samples);
                    if (_die) {
                        _audioDevice.stop();
                        return;
                    }
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        _die = true;
    }
}
