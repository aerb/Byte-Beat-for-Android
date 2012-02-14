package com.tasty.fish;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.System;
import java.util.Random;

public class ByteBeatActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener, OnClickListener,
		OnItemSelectedListener {
	/** Called when the activity is first created. */
	static Random rand;
	static float timescale = (float) 1;
	static float p1 = 1;
	static float p2 = 1;
	static float p3 = 1;
	static boolean die = false;
	static SeekBar speed;
	static SeekBar param1;
	static SeekBar param2;
	static SeekBar param3;
	static TextView text0;
	static TextView text1;
	static TextView text2;
	static TextView text3;
	static Spinner spinner;
	static Button stop;
	static Button reset;
	static SStackView stackView;
	static float t = 0;
	static int beatMachineIndexer = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text0 = (TextView) findViewById(R.id.text0);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		speed = (SeekBar) findViewById(R.id.seekBar1);
		param1 = (SeekBar) findViewById(R.id.seekBar2);
		param2 = (SeekBar) findViewById(R.id.seekBar3);
		param3 = (SeekBar) findViewById(R.id.seekBar4);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bytes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		stackView = (SStackView) findViewById(R.id.imageView1);

		stop = (Button) findViewById(R.id.button1);
		stop.setOnClickListener(this);

		reset = (Button) findViewById(R.id.button2);
		reset.setOnClickListener(this);

		speed.setOnSeekBarChangeListener(this);
		param1.setOnSeekBarChangeListener(this);
		param2.setOnSeekBarChangeListener(this);
		param3.setOnSeekBarChangeListener(this);

		rand = new Random();

		new Thread(new Runnable() {
			public void run() {
				AndroidAudioDevice device = new AndroidAudioDevice();
				byte samples[] = new byte[1024];

				while (true) {
					for (int n = 0; n < samples.length; n++) {
						t += timescale;
						byte first = (byte) f((int) t, beatMachineIndexer);
						// byte next = (byte) f((int) t + 1,
						// beatMachineIndexer);
						// float slope = (next - samples[n])/(1/timescale);
						// for (int m = 0; m < 1 / timescale; ++m) {
						// ++n;
						// if(n >= samples.length) break;
						// samples[n] = (byte) (first + slope*m);
						// }
						samples[n] = first;
					}

					device.writeSamples(samples);
					stackView.setSamples(samples);
					stackView.updateT((int) t);
					stackView.postInvalidate();

					if (die) {
						device.stop();
						die = false;
						return;
					}
				}
			}
		}).start();
	}

	private static int f(int t, int index) {
		switch (index) {
		case 1:
			return (t * (int) (p1 * 5) & t >> (7))
					| (t * (int)(p2*3) & (int) (p3 * t) >> 10);
		case 2:
			return (t * (t >> (int) (5 * p1) | t >> (int) (p2 * 8))) >> (t >> (int) (p3 * 16));
		case 3:
			return t
					* (((t >> (int) (12 * p1)) | (t >> (int) (p2 * 8))) & ((int) (p3 * 63) & (t >> 4)));
		case 4:
			return ((t >> (int) (4 * p1)) | (t << 4) & (int) (100 * p2) + t
					/ (int) (p3 * 1000));
		case 5:
			return ((t >> (int) (p1 * 7)) * 0xff) * (t & (int) (p2 * 0xff))
					/ 32 & ~(t / (int) (p3 * 127));
		case 6:
			return t < 700000 ? ((t / (int) (p1 * 2000) % 2 != 0 ? t << 3
					: t << 2) >> (t / 10000 % 3) << t / 100000)
					: t < 800000 ? -t * t * t >> (t / (int) (p2 * 1000)) | t
							/ (800000 - t) : t < 805000 ? t << (t / 500)
							: (int) (1 * p3 / (t - t));
		}
		return 0;
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		if (arg0 == speed) {
			float inc = ((float) arg1) / 100;
			ByteBeatActivity.timescale = inc;
			text0.setText("Speed = " + inc);
		} else if (arg0 == param1) {
			float x = ((float) arg1) / 100 * 2;
			p1 = x;
			text1.setText("Arg0 = " + x);
		} else if (arg0 == param2) {
			float x = ((float) arg1) / 100 * 2;
			p2 = x;
			text2.setText("Arg1 = " + x);
		} else if (arg0 == param3) {
			float x = ((float) arg1) / 100 * 2;
			p3 = x;
			text3.setText("Arg2 = " + x);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == stop) {
			die = true;
			while (die) {
			}
			this.finish();
		} else if (arg0 == reset) {
			t = 0;
		}

	}

	public void onPause() {
		super.onPause();
		die = true;
		while (die) {
		}
		this.finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		beatMachineIndexer = arg2 + 1;
		System.out.println(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
