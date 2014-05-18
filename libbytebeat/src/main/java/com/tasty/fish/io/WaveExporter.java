package com.tasty.fish.io;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WaveExporter {

    private WaveHeader _header;
    private int _byteCount;
    private RandomAccessFile _out;

    private void writeWaveHeader() throws IOException {
        _header = new WaveHeader();
        _header.setPcmPayloadSize(_byteCount);
        _header.write(_out);
    }

    public void init(String path) throws IOException {
        _byteCount = 0;
        _out = new RandomAccessFile(path, "rw");
        writeWaveHeader();
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + _header != null ? _header.toString() : "";
    }

    public void write(byte[] data) throws IOException {
        _out.write(data);
        _byteCount += data.length;
    }

    public void close() throws IOException {
        _out.seek(0);
        writeWaveHeader();
        _out.close();
    }
}