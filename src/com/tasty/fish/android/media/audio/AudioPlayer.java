package com.tasty.fish.android.media.audio;


import com.tasty.fish.domain.IExpressionEvaluator;

public class AudioPlayer implements IAudioPlayer {

    byte samples[];
    private final AndroidAudioDevice _audioDevice;
    private final IExpressionEvaluator _evaluator;
    private boolean _die;

    public AudioPlayer(IExpressionEvaluator evaluator) {
        _audioDevice = new AndroidAudioDevice();
        samples = new byte[_audioDevice.getBufferSize()];

        _evaluator = evaluator;
        _die = false;
    }

    @Override
    public void startAudio() {
        _die = false;
        new Thread(new Runnable() {
            public void run() {
                _audioDevice.play();
                while (true) {
                    for (int i = 0; i < samples.length; i++){
                        samples[i] = _evaluator.getNextSample();
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
}
