package com.tasty.fish.io;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

import java.io.IOException;
import java.io.InputStream;

public class WaveExporter {

    public void Encode(byte[] data){
        WaveHeader header = new WaveHeader();
        header.setSampleRate(22055);
        header.setChannels(1);

        Wave w = new Wave(header, data);

        WaveFileManager io = new WaveFileManager(w);
        io.saveWaveAsFile("what.wav");
    }
}