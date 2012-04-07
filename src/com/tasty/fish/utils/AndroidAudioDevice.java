package com.tasty.fish.utils;

import android.media.*;

public class AndroidAudioDevice {
	// 44100,22050,11025,8000
	int sampleRate = 44100;
	int format = AudioFormat.ENCODING_PCM_8BIT;
	AudioTrack track;

	public AndroidAudioDevice() {
		int bufferSize = AudioTrack.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_CONFIGURATION_MONO, format)*3;
		track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				AudioFormat.CHANNEL_CONFIGURATION_MONO, format, bufferSize,
				AudioTrack.MODE_STREAM);
		track.play();
	}

	public void stop() {
		track.stop();
	}

	public void writeSamples(byte[] samples) {
		track.write(samples, 0, samples.length);
	}

}
