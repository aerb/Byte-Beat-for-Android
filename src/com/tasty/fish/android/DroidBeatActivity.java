package com.tasty.fish.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;

import com.tasty.fish.R;
import com.tasty.fish.android.fragments.keyboard.KeyboardFragment;
import com.tasty.fish.android.fragments.naming.CreateExpressionFragment;
import com.tasty.fish.android.fragments.parameters.ParametersFragment;
import com.tasty.fish.android.fragments.visuals.buffer.BufferVisualsFragment;
import com.tasty.fish.android.fragments.selection.ExpressionSelectionFragment;
import com.tasty.fish.android.fragments.visuals.expression.ExpressionFragment;
import com.tasty.fish.presenters.MediaControlsPresenter;
import com.tasty.fish.utils.CompositionRoot;
import com.tasty.fish.views.IAppController;
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
    private TextView _expressionTitleTextView;
    private CompositionRoot _root;
    private View _copyBtn;
    private View _addBtn;

    private View _keyboardContainer;
    private View _selectorContainer;
    private View _paramsContainer;
    private IAppController _appController;

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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.upperFragmentContainer, new ExpressionFragment())
                .add(R.id.mainBufferFragmentContainer, new BufferVisualsFragment())
                .add(R.id.mainParametersFragmentContainer, new ParametersFragment())
                .add(R.id.mainSelectorFragmentContainer, new ExpressionSelectionFragment())
                .add(R.id.mainKeyboardFragmentContainer, new KeyboardFragment())
                .commit();

        _expressionTitleTextView = (TextView)findViewById(R.id.expressionTitleTextView);

        _keyboardContainer = findViewById(R.id.mainKeyboardFragmentContainer);
        _selectorContainer = findViewById(R.id.mainSelectorFragmentContainer);
        _paramsContainer = findViewById(R.id.mainParametersFragmentContainer);

        _loadNewExpressionBtn = findViewById(R.id.selectNewExpressionLayout);
        _stopBtn = findViewById(R.id.buttonStop);
        _copyBtn = findViewById(R.id.mainCopyButton);
        _addBtn = findViewById(R.id.mainAddButton);

        _loadNewExpressionBtn.setOnClickListener(this);
        _stopBtn.setOnClickListener(this);
        _copyBtn.setOnClickListener(this);
        _addBtn.setOnClickListener(this);

        _appController = getCompositionRoot().getAppController();
        ((AppController)_appController).setActivity(this);
        _appController.ShowParams();

        _mediaControlsPresenter = _root.getMediaControlsPresenter();
        _mediaControlsPresenter.setView(this);
        registerIMediaControlsListener(_mediaControlsPresenter);
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
            _appController.ShowSelector();
        } else if(arg0 == _addBtn){
            new CreateExpressionFragment().show(getSupportFragmentManager(),"NamingDialog");
        } else if(arg0 == _copyBtn){
            CreateExpressionFragment frag = new CreateExpressionFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(CreateExpressionFragment.CopyArguement, true);
            frag.setArguments(bundle);
            frag.show(getSupportFragmentManager(), "NamingDialog");
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

    public void registerIMediaControlsListener(IMediaControlsListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void setTitle(String title) {
        _expressionTitleTextView.setText(title);
    }

    @Override
    public void OnRequestEdit() {
        _keyboardContainer.setVisibility(View.VISIBLE);
    }

    //endregion

    //region IAppController methods
    public void ShowKeyboard() {
        _keyboardContainer.setVisibility(View.VISIBLE);
        _selectorContainer.setVisibility(View.INVISIBLE);
        _paramsContainer.setVisibility(View.INVISIBLE);
    }

    public void ShowParams() {
        _keyboardContainer.setVisibility(View.INVISIBLE);
        _selectorContainer.setVisibility(View.INVISIBLE);
        _paramsContainer.setVisibility(View.VISIBLE);
    }

    public void ShowSelector() {
        _keyboardContainer.setVisibility(View.INVISIBLE);
        _selectorContainer.setVisibility(View.VISIBLE);
        _paramsContainer.setVisibility(View.INVISIBLE);
    }
    //endregion
}
