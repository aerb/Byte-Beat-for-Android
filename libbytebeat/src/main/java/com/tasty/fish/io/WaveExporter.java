package com.tasty.fish.io;

import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;

import java.io.IOException;

public class WaveExporter {

    private WaveFileManager wfm;
    private WaveHeader _header;
    private long _byteCount;
    private int bytesPerSample = 1;

    private void writeWaveHeader() throws IOException {
        _header = new WaveHeader();
        _header.setAudioFormat(1);
        _header.setBitsPerSample(8*bytesPerSample);
        _header.setChannels(1);
        _header.setBlockAlign(1);
        _header.setSampleRate(22055);
        _header.setByteRate(_header.getSampleRate()*bytesPerSample);
        System.out.println("BYTes ====> " + _byteCount);
        _header.setSubChunk2Size(_byteCount);
        _header.setChunkSize(_header.getSubChunk2Size() - 8);
        _header.setBlockAlign(_header.getChannels() * bytesPerSample);
        wfm.writeHeader(_header);
    }

    public void init(String path) throws IOException {
        _byteCount = 0;
        wfm = new WaveFileManager(path);
        writeWaveHeader();
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + _header != null ? _header.toString() : "";
    }

    public void write(byte[] data) throws IOException {
        wfm.writeData(data);
        _byteCount += data.length;
    }

    public void close() throws IOException {
        wfm.seek(0);
        writeWaveHeader();
        wfm.close();
    }
}