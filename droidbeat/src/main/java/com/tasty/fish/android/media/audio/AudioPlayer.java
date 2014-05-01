package com.tasty.fish.android.media.audio;


import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.io.WaveExporter;

import java.io.IOException;

public class AudioPlayer implements IAudioPlayer {

    byte _samples[];
    private final AndroidAudioDevice _audioDevice;
    private final IExpressionEvaluator _evaluator;
    private boolean _die;
    private WaveExporter _exporter;

    public AudioPlayer(IExpressionEvaluator evaluator) {
        _audioDevice = new AndroidAudioDevice();

        _samples = new byte[512];

        _evaluator = evaluator;
        _die = false;
    }

    public byte[] getBuffer(){
        return _samples;
    }

    public void startAndRecord(String path) throws IOException {
        initializeFile(path);
        start();
    }

    private void initializeFile(String path) throws IOException {
        _exporter = new WaveExporter();
        _exporter.init(path);
    }

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
                    if(_exporter != null)
                        try {
                            _exporter.write(_samples);
                        } catch (IOException e) {
                            e.printStackTrace();
                            _die = true;
                        }
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
        if(_exporter != null){
            try {
                _exporter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            _exporter = null;
        }
    }
}
