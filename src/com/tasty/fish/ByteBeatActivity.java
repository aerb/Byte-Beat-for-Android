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
import java.util.ArrayList;
import java.util.Random;

public class ByteBeatActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener, OnClickListener,
		OnItemSelectedListener {
	/** Called when the activity is first created. */
	static Random rand;
	static float timescale = (float) 0.5;
	static float timeParams[] = { 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f };
	static float p1 = 1;
	static float p2 = 1;
	static float p3 = 1;
	static boolean die = true;
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
	static Button resetargs;
	static SStackView stackView;
	static float t = 0;
	static int beatMachineIndexer = 0;
	static int previousIndexer = 0;
	static float[][] byteParams = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 },
			{ 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
	static String[] eqadapter = {
			"((t%(int)(p1*777))|(int)(p3*t)) & ((int)(0xFF*p2))-t",
			"(( (p1 * t) >> 1 %  (p2 * 128)) + 20) * 3 * t >> 14 * t >>  (p3 * 18)",
			"t* (((t >> 9) &  (p3 * 10)) | (( (p2 * t) >> 11) & 24)^ ((t >> 10) & 15 & ( (p1 * t) >> 15)))",
			" (p1 * t) * 5 & ( (p2 * t) >> 7)|  (p3 * t * 3) & (t * 4 >> 10)",
			"(( (p1 * t) * ( (p2 * t) >> 8 | t >> 9)&  (p3 * 46) & t >> 8))^ (t & t >> 13 | t >> 6)",
			"( (p1 * t) * ( (p2 * t) >> 5 | t >> 8)) >> ( (p3 * t) >> 16)" };

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

		resetargs = (Button) findViewById(R.id.button3);
		resetargs.setOnClickListener(this);

		speed.setOnSeekBarChangeListener(this);
		param1.setOnSeekBarChangeListener(this);
		param2.setOnSeekBarChangeListener(this);
		param3.setOnSeekBarChangeListener(this);

		rand = new Random();
	}

	private void startAudioThread() {
		die = false;

		new Thread(new Runnable() {
			public void run() {
				AndroidAudioDevice device = new AndroidAudioDevice();
				byte samples[] = new byte[1024];
				stackView.setSamples(samples);
				while (true) {
					for (int n = 0; n < samples.length; n++) {
						t += timescale;
						samples[n] = (byte) f((int) t, beatMachineIndexer);
					}

					device.writeSamples(samples);

					stackView.updateT((int) t);
					stackView.postInvalidate();

					if (die) {
						device.stop();
						return;
					}
				}
			}
		}).start();
	}

	private static int f(int t, int index) {
		switch (index) {
		case 0:
			return ((t%(int)(p1*777))|(int)(p3*t)) & ((int)(0xFF*p2))-t;
//			return (t * (int) (p1 * 5) & t >> (7))
//					| (t * (int) (p2 * 3) & (int) (p3 * t) >> 10);
		case 1:
			return (((int) (p1 * t) >> 1 % (int) (p2 * 128)) + 20) * 3 * t >> 14 * t >> (int) (p3 * 18);
		case 2:
			return t
					* (((t >> 9) & (int) (p3 * 10)) | (((int) (p2 * t) >> 11) & 24)
							^ ((t >> 10) & 15 & ((int) (p1 * t) >> 15)));
		case 3:
			return (int) (p1 * t) * 5 & ((int) (p2 * t) >> 7)
					| (int) (p3 * t * 3) & (t * 4 >> 10);
		case 4:
			return (((int) (p1 * t) * ((int) (p2 * t) >> 8 | t >> 9)
					& (int) (p3 * 46) & t >> 8))
					^ (t & t >> 13 | t >> 6);
		case 5:
			return ((int) (p1 * t) * ((int) (p2 * t) >> 5 | t >> 8)) >> ((int) (p3 * t) >> 16);
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
			x = (float) (x == 0 ? 0.01 : x);
			p1 = x;
			text1.setText("Arg0 = " + x);
		} else if (arg0 == param2) {
			float x = ((float) arg1) / 100 * 2;
			x = (float) (x == 0 ? 0.01 : x);
			p2 = x;
			text2.setText("Arg1 = " + x);
		} else if (arg0 == param3) {
			float x = ((float) arg1) / 100 * 2;
			x = (float) (x == 0 ? 0.01 : x);
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
			if (die == false) {
				die = true;
				stop.setText("Start");
			} else {
				startAudioThread();
				stop.setText("Stop");
			}
			stop.refreshDrawableState();
		} else if (arg0 == reset) {
			t = 0;
		} else if (arg0 == resetargs) {
			timescale = 0.5f;
			p1 = 1;
			p2 = 1;
			p3 = 1;

			speed.setProgress((int) (timescale * 100));
			param1.setProgress((int) (p1 * 100 / 2));
			param2.setProgress((int) (p2 * 100 / 2));
			param3.setProgress((int) (p3 * 100 / 2));
		}
	}

	public void onPause() {
		super.onPause();
		die = true;
		stop.setText("Start");
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		timeParams[beatMachineIndexer] = timescale;
		byteParams[beatMachineIndexer][0] = p1;
		byteParams[beatMachineIndexer][1] = p2;
		byteParams[beatMachineIndexer][2] = p3;

		beatMachineIndexer = arg2;
		t = 0;
		timescale = timeParams[beatMachineIndexer];
		p1 = byteParams[arg2][0];
		p2 = byteParams[arg2][1];
		p3 = byteParams[arg2][2];

		speed.setProgress((int) (timescale * 100));
		param1.setProgress((int) (p1 * 100 / 2));
		param2.setProgress((int) (p2 * 100 / 2));
		param3.setProgress((int) (p3 * 100 / 2));

		stackView.updateEq(this.eqadapter[arg2]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
