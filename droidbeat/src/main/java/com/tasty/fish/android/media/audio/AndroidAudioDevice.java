package com.tasty.fish.android.media.audio;

import android.media.*;

public class AndroidAudioDevice {
    // 44100,22050,11025,8000
    int sampleRate = 22050;
    int format = AudioFormat.ENCODING_PCM_8BIT;
    int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    AudioTrack _track;

    private final int _bufferSize;

    private final int BASE_MULTIPLIER = 3;

    public AndroidAudioDevice() {
        int minSize = AudioTrack.getMinBufferSize(
                sampleRate,
                channelConfig,
                format)*BASE_MULTIPLIER;

        _bufferSize = minSize;

        _track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                channelConfig,
                format,
                _bufferSize,
                AudioTrack.MODE_STREAM);
    }

    public int getBufferSize(){
        return _bufferSize;
    }

    public void writeSamples(byte[] samples) {
        int total = 0;
        int length = samples.length;

        while(true){
            total += _track.write(samples, total, samples.length - total);
            if (total >= length)
                break;
        }
    }

    public void play(){
        _track.play();
    }

    public void stop() {
        _track.stop();
    }

}
