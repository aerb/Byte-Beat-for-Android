package com.tasty.fish;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.tasty.fish.R;
import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.domain.ByteBeatExpression.CompiledExpression;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.interfaces.IKeyboardDisplayView;
import com.tasty.fish.presenters.DroidBeatPresenter;
import com.tasty.fish.presenters.KeyboardPresenter;

public class DroidBeatView extends Activity implements
		SeekBar.OnSeekBarChangeListener, OnClickListener,
		OnItemSelectedListener, IDroidBeatView, IKeyboardDisplayView {

	private static boolean _die = true;
	private static boolean keyboardInputOn = false;
	private static SeekBar seekBarSpeed;
	private static SeekBar[] seekBarArgs = new SeekBar[3];
	private static TextView textSpeed;
	private static TextView[] textArgs = new TextView[3];
	private static Spinner spinner;
	private static Button switchViewButton;
	private static Button stop;
	private static Button resetTime;
	private static Button resetArgs;
	private static BufferView bufferView;
	private static DroidBeatPresenter _presenter;
	private static KeyboardPresenter _keyboardPresenter;

	private static String[] _predefinedTitles = { "bleullama-fun", "harism",
			"tangent128", "miiro", "xpansive", "tejeez" };

	private static String[] _predefinedExpressions = {
			"((t % (p1 * 777)) | (p3 * t)) & ((0xFF * p2)) - t",
			"(((p1 * t) >> 1 % (p2 * 128)) + 20) * 3 * t >> 14 * t >> (p3 * 18)",
			"t * (((t >> 9) & (p3 * 10)) | (((p2 * t) >> 11) & 24) ^ ((t >> 10) & 15 & ((p1 * t) >> 15)))",
			"(p1 * t) * 5 & ((p2 * t) >> 7) | (p3 * t * 3) & (t * 4 >> 10)",
			"(((p1 * t) * ((p2 * t) >> 8 | t >> 9) & (p3 * 46) & t >> 8)) ^ (t & t >> 13 | t >> 6)",
			"((p1 * t) * ((p2 * t) >> 5 | t >> 8)) >> ((p3 * t) >> 16)" };

	private static CompiledExpression[] _compiledExpressions = {
			new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) (((t % (int) (p1 * 777)) | (int) (p3 * t)) & (int) (0xFF * p2)
							- t);
				}
			}, new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) ((((int) (p1 * t) >> 1 % (int) (p2 * 128)) + 20)
							* 3 * t >> 14 * t >> (int) (p3 * 18));
				}
			}, new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) (t * (((t >> 9) & (int) (p3 * 10)) | (((int) (p2 * t) >> 11) & 24)
							^ ((t >> 10) & 15 & ((int) (p1 * t) >> 15))));
				}
			}, new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) ((int) (p1 * t) * 5 & ((int) (p2 * t) >> 7) | (int) (p3 * t)
							* 3 & (t * 4 >> 10));
				}
			}, new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) ((((int) (p1 * t)
							* ((int) (p2 * t) >> 8 | t >> 9)
							& ((int) (p3 * 46)) & t >> 8)) ^ (t & t >> 13 | t >> 6));
				}
			}, new CompiledExpression() {
				public byte evaluate(int t, double p1, double p2, double p3) {
					return (byte) (((int) (p1 * t) * ((int) (p2 * t) >> 5 | t >> 8)) >> ((int) (p3 * t) >> 16));
				}
			} };
	private LinearLayout inputLayout;
	private View keyboardView;
	private View paramView;
	private TextView textExpression;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		LayoutInflater inflater = getLayoutInflater();
		keyboardView = inflater.inflate(R.layout.keyboard, null);
		paramView = inflater.inflate(R.layout.params, null);

		_presenter = new DroidBeatPresenter(this);
		_keyboardPresenter = new KeyboardPresenter(this);

		for (int i = 0; i < _predefinedExpressions.length; ++i)
			_presenter.addNewExpression(_predefinedTitles[i],
					_predefinedExpressions[i]);

		_presenter.addNewExpression("custom", "t");

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item,
				_presenter.getExpressions());

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		textExpression = (TextView) findViewById(R.id.textExpression);
		inputLayout = (LinearLayout) findViewById(R.id.linearLayout3);
		switchViewButton = (Button) findViewById(R.id.buttonSwitchInput);
		textSpeed = (TextView) paramView.findViewById(R.id.textSpeed);
		textArgs[0] = (TextView) paramView.findViewById(R.id.textArg1);
		textArgs[1] = (TextView) paramView.findViewById(R.id.textArg2);
		textArgs[2] = (TextView) paramView.findViewById(R.id.textArg3);
		seekBarSpeed = (SeekBar) paramView.findViewById(R.id.seekBarSpeed);
		seekBarArgs[0] = (SeekBar) paramView.findViewById(R.id.seekBarArg1);
		seekBarArgs[1] = (SeekBar) paramView.findViewById(R.id.seekBarArg2);
		seekBarArgs[2] = (SeekBar) paramView.findViewById(R.id.seekBarArg3);
		spinner = (Spinner) findViewById(R.id.spinnerPredefined);
		resetArgs = (Button) paramView.findViewById(R.id.buttonResetArgs);
		resetTime = (Button) paramView.findViewById(R.id.buttonResetTime);
		stop = (Button) findViewById(R.id.buttonStop);
		bufferView = (BufferView) findViewById(R.id.bufferView);

		spinner.setAdapter(adapter);
		inputLayout.addView(paramView);

		switchViewButton.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
		stop.setOnClickListener(this);
		resetTime.setOnClickListener(this);
		resetArgs.setOnClickListener(this);
		seekBarSpeed.setOnSeekBarChangeListener(this);
		seekBarArgs[0].setOnSeekBarChangeListener(this);
		seekBarArgs[1].setOnSeekBarChangeListener(this);
		seekBarArgs[2].setOnSeekBarChangeListener(this);

		_presenter.setActiveExpression(0);
	}

	private double mapTimeSeekBar(double arg1) {
		return arg1 / 100;
	}

	private double mapArgSeekBar(double arg1) {
		double x = ((float) arg1) / 100 * 2;
		x = (float) (x == 0 ? 0.01 : x);
		return x;
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		if (arg0 == seekBarSpeed) {
			double inc = mapTimeSeekBar(arg1);
			_presenter.updateTimeScale(inc);
			textSpeed.setText("speed = " + inc);
		} else {
			double x = mapArgSeekBar(arg1);
			if (arg0 == seekBarArgs[0]) {
				_presenter.updateArgument(0, x);
				textArgs[0].setText(String.format("p1 = %.2f",x));
			} else if (arg0 == seekBarArgs[1]) {
				_presenter.updateArgument(1, x);
				textArgs[1].setText(String.format("p2 = %.2f",x));
			} else if (arg0 == seekBarArgs[2]) {
				_presenter.updateArgument(2, x);
				textArgs[2].setText(String.format("p3 = %.2f",x));
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == stop) {
			if (_die == false) {
				_presenter.stopAudioThread();
				stop.setText("Start");
				_die = true;
			} else {
				_presenter.startAudioThread();
				_presenter.startVideoThread();
				stop.setText("Stop");
				_die = false;
			}
			stop.refreshDrawableState();
		} else if (arg0 == resetTime) {
			_presenter.resetTime();
		} else if (arg0 == resetArgs) {
			_presenter.resetArgs();
		} else if (arg0 == switchViewButton) {
			keyboardInputOn = !keyboardInputOn;
			setKeyboardView(keyboardInputOn);
			if (keyboardInputOn) {
				spinner.setSelection(spinner.getCount() - 1);
			}
		}
	}

	private void setKeyboardView(boolean on) {
		keyboardInputOn = on;
		inputLayout.removeAllViews();
		inputLayout.addView(on ? keyboardView : paramView);
	}

	public void updateSeekerSpeedPostion(double value) {
		seekBarSpeed.setProgress((int) (value * 100));
	}

	public void updateSeekerPostion(int i, double value) {
		seekBarArgs[i].setProgress((int) (value * 100 / 2));
	}

	public void onPause() {
		super.onPause();
		_die = true;
		_presenter.stopAudioThread();
		stop.setText("Start");
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int pos,
			long id) {

		ByteBeatExpression e = (ByteBeatExpression) adapter
				.getItemAtPosition(pos);
		_presenter.setActiveExpression(pos);
		_keyboardPresenter.setEditableExpression(e);

		if (pos < spinner.getCount() - 1)
			setKeyboardView(false);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void displayBuffer(byte[] samples, int t) {
		bufferView.setSamples(samples);
		bufferView.updateT(t);
		bufferView.postInvalidate();
	}

	@Override
	public void postInvalidate() {
	}

	@Override
	public void updateT(int time) {

	}

	@Override
	public View getInflatedKeyboard() {
		return keyboardView;
	}

	private UnderlineSpan ul = new UnderlineSpan();

	@Override
	public void updateDisplayedExpression(String s, int cursor) {
		SpannableString content = new SpannableString(s);
		content.removeSpan(ul);
		content.setSpan(ul, cursor, cursor + 1, 0);
		textExpression.setText(content);
	}
}
