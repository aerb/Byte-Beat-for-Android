package com.tasty.fish.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;

import com.tasty.fish.R;
import com.tasty.fish.android.fragments.keyboard.KeyboardFragment;
import com.tasty.fish.android.fragments.parameters.ParametersFragment;
import com.tasty.fish.android.fragments.visuals.buffer.BufferVisualsFragment;
import com.tasty.fish.android.fragments.selection.ExpressionSelectionFragment;
import com.tasty.fish.android.fragments.visuals.expression.ExpressionFragment;
import com.tasty.fish.presenters.ExpressionPresenter;
import com.tasty.fish.presenters.MediaControlsPresenter;
import com.tasty.fish.utils.CompositionRoot;
import com.tasty.fish.views.IMediaControlsView;
import com.tasty.fish.views.IExpressionView;

import java.util.ArrayList;

public class DroidBeatActivity extends FragmentActivity implements
                                                       OnClickListener,
                                                       IExpressionView.IExpressionViewListener,
        IMediaControlsView
{
    private static boolean s_dieFlag = true;

    private LinearLayout _loadNewExpressionBtn;

    private static Button s_stopBtn;

    private MediaControlsPresenter _mediaControlsPresenter;

    private ArrayList<IMediaControlsListener> _listeners;
    private ExpressionPresenter _expressionPresenter;
    private TextView _expressionTitleTextView;
    private CompositionRoot _root;

    public CompositionRoot getCompositionRoot() {
        return _root != null ?
               _root :
              (_root = new CompositionRoot());
    }

    //region Activity methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        _listeners = new ArrayList<IMediaControlsListener>();

        getCompositionRoot();

        _mediaControlsPresenter = _root.getMediaControlsPresenter();
        _mediaControlsPresenter.setView(this);
        registerIDroidBeatViewListener(_mediaControlsPresenter);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lowerFragmentContainer, new ParametersFragment())
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.bufferFragmentContainer, new BufferVisualsFragment())
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.upperFragmentContainer, new ExpressionFragment())
                .commit();

        _expressionTitleTextView = (TextView)findViewById(R.id.expressionTitleTextView);
        _loadNewExpressionBtn = (LinearLayout) findViewById(R.id.selectNewExpressionLayout);
        s_stopBtn = (Button) findViewById(R.id.buttonStop);

        _loadNewExpressionBtn.setOnClickListener(this);
        s_stopBtn.setOnClickListener(this);
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
        } else if(arg0 == _loadNewExpressionBtn){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lowerFragmentContainer, new ExpressionSelectionFragment())
                    .commit();
        }
    }
    //endregion

    //region Notification methods
    private void NotifyStartPlay(){
        for(IMediaControlsListener listener : _listeners){
            listener.OnStartPlay();
        }
    }

    private void NotifyStopPlay(){
        for(IMediaControlsListener listener : _listeners){
            listener.OnStopPlay();
        }
    }

    //endregion

    //region IMediaControlsView methods

    public void registerIDroidBeatViewListener(IMediaControlsListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setTitle(String title) {
        _expressionTitleTextView.setText(title);
    }

    @Override
    public void OnRequestEdit() {
        loadKeyboardFragment();
    }
    //endregion

    private void loadKeyboardFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lowerFragmentContainer, new KeyboardFragment())
                .commit();
    }
}
