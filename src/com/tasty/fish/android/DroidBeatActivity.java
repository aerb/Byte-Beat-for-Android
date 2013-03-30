package com.tasty.fish.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private View _loadNewExpressionBtn;

    private View _stopBtn;

    private MediaControlsPresenter _mediaControlsPresenter;

    private ArrayList<IMediaControlsListener> _listeners;
    private ExpressionPresenter _expressionPresenter;
    private TextView _expressionTitleTextView;
    private CompositionRoot _root;
    private View _refreshBtn;
    private View _paramsBtn;

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
                .add(R.id.upperFragmentContainer, new ExpressionFragment())
                .add(R.id.mainBufferFragmentContainer, new BufferVisualsFragment())
                .add(R.id.mainActionsFragmentContainer, new ParametersFragment())
                .commit();

        _expressionTitleTextView = (TextView)findViewById(R.id.expressionTitleTextView);

        _loadNewExpressionBtn = findViewById(R.id.selectNewExpressionLayout);
        _stopBtn = findViewById(R.id.buttonStop);
        _refreshBtn = findViewById(R.id.refreshButton);
        _paramsBtn = findViewById(R.id.parametersButton);

        _loadNewExpressionBtn.setOnClickListener(this);
        _stopBtn.setOnClickListener(this);
        _refreshBtn.setOnClickListener(this);
        _paramsBtn.setOnClickListener(this);
    }

    public void onPause() {
        super.onPause();
        s_dieFlag = true;
        NotifyStopPlay();
    }
    //endregion

    //region OnClickListener methods
    @Override
    public void onClick(View arg0) {
        if (arg0 == _stopBtn) {
            if (s_dieFlag == false) {
                s_dieFlag = true;
                NotifyStopPlay();
            } else {
                s_dieFlag = false;
                NotifyStartPlay();
            }
        } else if(arg0 == _loadNewExpressionBtn){
            setActionsFragment(new ExpressionSelectionFragment());
        } else if(arg0 == _paramsBtn){
            setActionsFragment(new ParametersFragment());
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

    private void setActionsFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActionsFragmentContainer, fragment)
                .commit();
    }

    private void loadKeyboardFragment() {
        setActionsFragment(new KeyboardFragment());
    }
}
