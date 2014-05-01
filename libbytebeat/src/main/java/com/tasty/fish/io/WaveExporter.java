package com.tasty.fish.io;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WaveExporter {

    private WaveFileManager wfm;

    public void init() throws IOException {
        WaveHeader header = new WaveHeader();
        header.setSampleRate(22055);
        header.setChannels(1);

        wfm = new WaveFileManager("what.wav");
        wfm.writeHeader(header);
    }

    public void write(byte[] data) throws IOException {
        wfm.writeData(data);
    }

    public void close() throws IOException {
        wfm.close();
    }
}