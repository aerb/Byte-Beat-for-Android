package com.tasty.fish.android.media.audio;

public interface IAudioPlayer {
    void start();
    void stop();

    byte[] getBuffer();
}
