package com.tasty.fish.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Set;

public class WaveHeader {

    private int _pcmPayload = 0;
    private LinkedHashMap<String, byte[]> _headers;

    public void setPcmPayloadSize(int bytes){
        _pcmPayload = bytes;
        _headers = new LinkedHashMap<String, byte[]>();
    }

    public void write(RandomAccessFile out) throws IOException {
        short numChannels = 1;
        int sampleRate = 22050;
        int bytesPerSample = 1;

        _headers.put("ChunkID",         toBigEndian("RIFF"));
        _headers.put("ChunkSize",       toLittleEndian4(_pcmPayload + 36));
        _headers.put("Format",          toBigEndian("WAVE"));

        _headers.put("Subchunk1ID",     toBigEndian("fmt "));
        _headers.put("Subchunk1Size",   toLittleEndian4(16));
        _headers.put("AudioFormat",     toLittleEndian2((short) 1)); // 1=PCM
        _headers.put("NumChannels",     toLittleEndian2(numChannels));
        _headers.put("SampleRate",      toLittleEndian4(sampleRate));
        _headers.put("ByteRate",        toLittleEndian4(sampleRate * numChannels * bytesPerSample));
        _headers.put("BlockAlign",      toLittleEndian2((short) (numChannels * bytesPerSample)));
        _headers.put("BitsPerSample",   toLittleEndian2((short) (bytesPerSample * 8)));

        _headers.put("Subchunk2ID",     toBigEndian("data"));
        _headers.put("Subchunk2Size",   toLittleEndian4(_pcmPayload));

        Set<String> keys = _headers.keySet();
        for(String key : keys) out.write(_headers.get(key));
        _headers.clear();
    }

    private byte[] toBigEndian(String value){
        return value.getBytes();
    }

    private byte[] toLittleEndian4(int value){
        return new byte[] {
            (byte)((value)),
            (byte)((value >>  8) & 0xFF),
            (byte)((value >> 16) & 0xFF),
            (byte)((value >> 24) & 0xFF)};
    }

    private byte[] toLittleEndian2(short value){
        return new byte[] {
            (byte)((value)),
            (byte)((value >>  8) & 0xFF)};
    }
}
