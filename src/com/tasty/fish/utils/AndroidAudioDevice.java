package com.tasty.fish.utils;

import android.media.*;

public class AndroidAudioDevice {
    // 44100,22050,11025,8000
    int sampleRate = 22050;
    int format = AudioFormat.ENCODING_PCM_8BIT;
    AudioTrack _track;
    private final int _bufferSize;

    public AndroidAudioDevice() {
        _bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                format)*5;
        _track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                format,
                _bufferSize,
                AudioTrack.MODE_STREAM);
    }

    public int getBufferSize(){
        return _bufferSize;
    }

    public void writeSamples(byte[] samples) {
        int err = _track.write(samples, 0, samples.length);
        if(err < 0){
            System.out.println(err);
        }
    }

    public void play(){
        _track.play();
    }

    public void stop() {
        _track.stop();
    }

}
