package com.tasty.fish.io;

import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

import java.io.IOException;

public class WaveExporter {

    private WaveFileManager wfm;
    private WaveHeader _header;

    public void init(String path) throws IOException {
        _header = new WaveHeader();

        int bytesPerSample = 1;
        _header.setByteRate(_header.getChannels()* _header.getSampleRate()*bytesPerSample);
        _header.setAudioFormat(1);
        _header.setBitsPerSample(8*bytesPerSample);
        _header.setChannels(1);
        _header.setBlockAlign(1);
        _header.setSampleRate(22055);

        _header.setSubChunk2Size(1000000);
        _header.setChunkSize(_header.getSubChunk2Size() - 8);

        _header.setBlockAlign(_header.getChannels() * bytesPerSample);

        wfm = new WaveFileManager(path);
        wfm.writeHeader(_header);
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + _header != null ? _header.toString() : "";
    }

    public void write(byte[] data) throws IOException {
        wfm.writeData(data);
    }

    public void close() throws IOException {
        wfm.close();
    }
}