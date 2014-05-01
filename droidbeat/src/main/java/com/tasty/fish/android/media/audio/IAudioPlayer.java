package com.tasty.fish.android.media.audio;

import java.io.IOException;

public interface IAudioPlayer {
    void start();
    void startAndRecord(String path) throws IOException;

    void stop();

    byte[] getBuffer();
}
