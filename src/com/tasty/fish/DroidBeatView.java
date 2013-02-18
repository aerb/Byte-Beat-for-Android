package com.tasty.fish;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.domain.ExpressionEvaluator;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.presenters.DroidBeatPresenter;

import java.util.ArrayList;

public class DroidBeatView extends FragmentActivity implements
                                                       OnClickListener,
                                                       OnItemSelectedListener,
                                                       IDroidBeatView
{
    private static boolean s_dieFlag = true;
    private static boolean s_keyboardInputOnFlag = false;

    private static Button s_loadBtn;

    private static Button s_switchViewBtn;
    private static Button s_stopBtn;

    private DroidBeatPresenter _droidbeatPresenter;

    private LinearLayout m_inputLayout;

    private TextView m_textExpressionView;

    private ArrayList<IDroidBeatViewListener> _listeners;

    private void setEditorView(boolean on) {

    }

    //region Activity methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        _listeners = new ArrayList<IDroidBeatViewListener>();

        _droidbeatPresenter = new DroidBeatPresenter(this, new ExpressionEvaluator());

        m_textExpressionView = (TextView) findViewById(R.id.textExpression);
        m_inputLayout = (LinearLayout) findViewById(R.id.viewFrame);
        s_switchViewBtn = (Button) findViewById(R.id.buttonSwitchInput);
        s_loadBtn = (Button) findViewById(R.id.loadView);
        s_stopBtn = (Button) findViewById(R.id.buttonStop);


        //m_inputLayout.addView(m_parameterView);

        s_switchViewBtn.setOnClickListener(this);
        s_loadBtn.setOnClickListener(this);
        s_stopBtn.setOnClickListener(this);

        NotifyExpressionChanged(0);
    }
    public void onPause() {
        super.onPause();
        s_dieFlag = true;
        s_stopBtn.setText("Start");
        NotifyStopPlay();
    }
    //endregion

    //region OnClickListener methods
    @Override
    public void onClick(View arg0) {
        if (arg0 == s_stopBtn) {
            if (s_dieFlag == false) {
                s_stopBtn.setText("Start");
                s_dieFlag = true;
                NotifyStopPlay();
            } else {
                s_stopBtn.setText("Stop");
                s_dieFlag = false;
                NotifyStartPlay();
            }
            s_stopBtn.refreshDrawableState();
        } else if (arg0 == s_switchViewBtn) {
            s_keyboardInputOnFlag = !s_keyboardInputOnFlag;
            setEditorView(s_keyboardInputOnFlag);
        } else if(arg0 == s_loadBtn){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.viewFrame, new ExpressionSelectionView())
                    .commit();
        }
    }
    //endregion

    //region OnItemSelectedListener methods
    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int pos,
            long id) {

        ByteBeatExpression e = (ByteBeatExpression) adapter
                .getItemAtPosition(pos);

        NotifyExpressionChanged(pos);

        //s_editorPresenter.setEditableExpression(e);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}
    //endregion

    //region Notification methods
    private void NotifyStartPlay(){
        for(IDroidBeatViewListener listener : _listeners){
            listener.OnStartPlay();
        }
    }

    private void NotifyStopPlay(){
        for(IDroidBeatViewListener listener : _listeners){
            listener.OnStopPlay();
        }
    }

    private void NotifyExpressionChanged(int id){
        for(IDroidBeatViewListener listener : _listeners){
            listener.OnExpressionChanged(id);
        }
    }
    //endregion

    //region IDroidBeatView methods

    public void registerIDroidBeatViewListener(IDroidBeatViewListener listener) {
        _listeners.add(listener);
    }

    public DroidBeatPresenter getDroidbeatPresenter() {
        return _droidbeatPresenter;
    }
    //endregion
}
