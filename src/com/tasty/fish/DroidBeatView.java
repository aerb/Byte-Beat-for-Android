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

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.interfaces.IKeyboardDisplayView;
import com.tasty.fish.presenters.DroidBeatPresenter;
import com.tasty.fish.presenters.KeyboardPresenter;

public class DroidBeatView extends Activity implements SeekBar.OnSeekBarChangeListener, 
                                                       OnClickListener,
                                                       OnItemSelectedListener,
                                                       IDroidBeatView,
                                                       IKeyboardDisplayView {
    private static boolean s_dieFlag = true;
    private static boolean s_keyboardInputOnFlag = false;

    private static SeekBar s_seekBarSpeed;
    private static SeekBar[] s_seekBarArgs = new SeekBar[3];

    private static TextView s_textSpeed;
    private static TextView[] s_textArgs = new TextView[3];

    private static Spinner s_spinner;

    private static Button s_switchViewBtn;
    private static Button s_stopBtn;
    private static Button s_resetTimeBtn;
    private static Button s_resetArgsBtn;

    private static BufferView s_bufferView;
    private static DroidBeatPresenter s_performancePresenter;
    private static KeyboardPresenter s_editorPresenter;

    private UnderlineSpan m_underlineSpan = new UnderlineSpan();

    private LinearLayout m_inputLayout;

    private View m_editorView;
    private View m_parameterView;
    private TextView m_textExpressionView;

    private static String[] s_predefinedTitles = { "bleullama-fun", "harism",
            "tangent128", "miiro", "xpansive", "tejeez" };

    private static String[] s_predefinedExpressions = {
            "((t % (p1 * 777)) | (p3 * t)) & ((0xFF * p2)) - t",
            "(((p1 * t) >> 1 % (p2 * 128)) + 20) * 3 * t >> 14 * t >> (p3 * 18)",
            "t * (((t >> 9) & (p3 * 10)) | (((p2 * t) >> 11) & 24) ^ ((t >> 10) & 15 & ((p1 * t) >> 15)))",
            "(p1 * t) * 5 & ((p2 * t) >> 7) | (p3 * t * 3) & (t * 4 >> 10)",
            "(((p1 * t) * ((p2 * t) >> 8 | t >> 9) & (p3 * 46) & t >> 8)) ^ (t & t >> 13 | t >> 6)",
            "((p1 * t) * ((p2 * t) >> 5 | t >> 8)) >> ((p3 * t) >> 16)" };

    private double mapArgSeekBar(double arg1) {
        double x = ((float) arg1) / 100 * 2;
        x = (float) (x == 0 ? 0.01 : x);
        return x;
    }

    private double mapTimeSeekBar(double arg1) {
        return arg1 / 100;
    }

    private void setEditorView(boolean on) {
        s_keyboardInputOnFlag = on;
        m_inputLayout.removeAllViews();
        m_inputLayout.addView(on ? m_editorView : m_parameterView);
    }

    public void updateSeekerSpeedPostion(double value) {
        s_seekBarSpeed.setProgress((int) (value * 100));
    }

    public void updateSeekerPostion(int i, double value) {
        s_seekBarArgs[i].setProgress((int) (value * 100 / 2));
    }

    //region Activity methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        LayoutInflater inflater = getLayoutInflater();
        m_editorView = inflater.inflate(R.layout.keyboard, null);
        m_parameterView = inflater.inflate(R.layout.params, null);

        s_performancePresenter = new DroidBeatPresenter(this);
        s_editorPresenter = new KeyboardPresenter(this);

        for (int i = 0; i < s_predefinedExpressions.length; ++i)
            s_performancePresenter.addNewExpression(s_predefinedTitles[i],
                    s_predefinedExpressions[i]);

        s_performancePresenter.addNewExpression("custom", "t");

        @SuppressWarnings({ "rawtypes", "unchecked" })
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                s_performancePresenter.getExpressions());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_textExpressionView = (TextView) findViewById(R.id.textExpression);
        m_inputLayout = (LinearLayout) findViewById(R.id.linearLayout3);
        s_switchViewBtn = (Button) findViewById(R.id.buttonSwitchInput);
        s_textSpeed = (TextView) m_parameterView.findViewById(R.id.textSpeed);
        s_textArgs[0] = (TextView) m_parameterView.findViewById(R.id.textArg1);
        s_textArgs[1] = (TextView) m_parameterView.findViewById(R.id.textArg2);
        s_textArgs[2] = (TextView) m_parameterView.findViewById(R.id.textArg3);
        s_seekBarSpeed = (SeekBar) m_parameterView.findViewById(R.id.seekBarSpeed);
        s_seekBarArgs[0] = (SeekBar) m_parameterView.findViewById(R.id.seekBarArg1);
        s_seekBarArgs[1] = (SeekBar) m_parameterView.findViewById(R.id.seekBarArg2);
        s_seekBarArgs[2] = (SeekBar) m_parameterView.findViewById(R.id.seekBarArg3);
        s_spinner = (Spinner) findViewById(R.id.spinnerPredefined);
        s_resetArgsBtn = (Button) m_parameterView.findViewById(R.id.buttonResetArgs);
        s_resetTimeBtn = (Button) m_parameterView.findViewById(R.id.buttonResetTime);
        s_stopBtn = (Button) findViewById(R.id.buttonStop);
        s_bufferView = (BufferView) findViewById(R.id.bufferView);

        s_spinner.setAdapter(adapter);
        m_inputLayout.addView(m_parameterView);

        s_switchViewBtn.setOnClickListener(this);
        s_spinner.setOnItemSelectedListener(this);
        s_stopBtn.setOnClickListener(this);
        s_resetTimeBtn.setOnClickListener(this);
        s_resetArgsBtn.setOnClickListener(this);
        s_seekBarSpeed.setOnSeekBarChangeListener(this);
        s_seekBarArgs[0].setOnSeekBarChangeListener(this);
        s_seekBarArgs[1].setOnSeekBarChangeListener(this);
        s_seekBarArgs[2].setOnSeekBarChangeListener(this);

        s_performancePresenter.setActiveExpression(0);
    }
    public void onPause() {
        super.onPause();
        s_dieFlag = true;
        s_performancePresenter.stopAudioThread();
        s_stopBtn.setText("Start");
    }
    //endregion

    //region OnSeekBarChangeListeners methods
    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        if (arg0 == s_seekBarSpeed) {
            double inc = mapTimeSeekBar(arg1);
            s_performancePresenter.updateTimeScale(inc);
            s_textSpeed.setText("speed = " + inc);
        } else {
            double x = mapArgSeekBar(arg1);
            if (arg0 == s_seekBarArgs[0]) {
                s_performancePresenter.updateArgument(0, x);
                s_textArgs[0].setText(String.format("p1 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[1]) {
                s_performancePresenter.updateArgument(1, x);
                s_textArgs[1].setText(String.format("p2 = %.2f", x));
            } else if (arg0 == s_seekBarArgs[2]) {
                s_performancePresenter.updateArgument(2, x);
                s_textArgs[2].setText(String.format("p3 = %.2f", x));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {}

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {}
    //endregion

    //region OnClickListener methods
    @Override
    public void onClick(View arg0) {
        if (arg0 == s_stopBtn) {
            if (s_dieFlag == false) {
                s_performancePresenter.stopAudioThread();
                s_stopBtn.setText("Start");
                s_dieFlag = true;
            } else {
                s_performancePresenter.startAudioThread();
                s_performancePresenter.startVideoThread();
                s_stopBtn.setText("Stop");
                s_dieFlag = false;
            }
            s_stopBtn.refreshDrawableState();
        } else if (arg0 == s_resetTimeBtn) {
            s_performancePresenter.resetTime();
        } else if (arg0 == s_resetArgsBtn) {
            s_performancePresenter.resetArgs();
        } else if (arg0 == s_switchViewBtn) {
            s_keyboardInputOnFlag = !s_keyboardInputOnFlag;
            setEditorView(s_keyboardInputOnFlag);
            if (s_keyboardInputOnFlag) {
                s_spinner.setSelection(s_spinner.getCount() - 1);
            }
        }
    }
    //endregion

    //region OnItemSelectedListener methods
    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int pos,
            long id) {

        ByteBeatExpression e = (ByteBeatExpression) adapter
                .getItemAtPosition(pos);
        s_performancePresenter.setActiveExpression(pos);
        s_editorPresenter.setEditableExpression(e);

        if (pos < s_spinner.getCount() - 1)
            setEditorView(false);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    //endregion

    //region IDroidBeatView methods
    @Override
    public void displayBuffer(byte[] samples, int t) {
        s_bufferView.setSamples(samples);
        s_bufferView.updateT(t);
        s_bufferView.postInvalidate();
    }

    @Override
    public void postInvalidate() {
    }

    @Override
    public void updateT(int time) {
    }

    @Override
    public void updateDisplayedExpression(String s, int cursor) {
        SpannableString content = new SpannableString(s);
        content.removeSpan(m_underlineSpan);
        content.setSpan(m_underlineSpan, cursor, cursor + 1, 0);
        m_textExpressionView.setText(content);
    }
    //endregion

    //region IKeyBoardDisplayView methods
    @Override
    public View getInflatedKeyboard() {
        return m_editorView;
    }
    //endregion
}
