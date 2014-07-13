package com.tasty.fish.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tasty.fish.R;
import com.tasty.fish.android.fragments.ExportFragment;
import com.tasty.fish.android.fragments.keyboard.KeyboardFragment;
import com.tasty.fish.android.fragments.naming.CreateExpressionFragment;
import com.tasty.fish.android.fragments.parameters.ParametersFragment;
import com.tasty.fish.android.fragments.selection.ExpressionListFragment;
import com.tasty.fish.android.fragments.visuals.buffer.BufferVisualsFragment;
import com.tasty.fish.android.fragments.visuals.expression.ExpressionFragment;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.presenters.ExpressionIO;
import com.tasty.fish.presenters.MediaControlsPresenter;
import com.tasty.fish.utils.CompositionRoot;
import com.tasty.fish.utils.Manifest;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IExpressionView;
import com.tasty.fish.views.IMediaControlsView;

import java.io.IOException;

public class DroidBeatActivity extends FragmentActivity implements
        OnClickListener,
        IExpressionView.IExpressionViewListener,
        IMediaControlsView
{
    private View _loadNewExpressionBtn;

    private View _startBtn;
    private View _stopBtn;
    private View _recordBtn;

    private MediaControlsPresenter _mediaControlsPresenter;

    private TextView _expressionTitleTextView;
    private CompositionRoot _root;
    private View _copyBtn;
    private View _pasteBtn;
    private View _addBtn;

    private View _keyboardContainer;
    private View _selectorContainer;
    private View _paramsContainer;
    private IAppController _appController;
    private ClipboardManager _clipboard;
    private IExpressionList _repo;
    private BufferVisualsFragment _bufferVisuals;
    private boolean _paidVersion;


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

        try {
            _paidVersion = Manifest.isPaidVersion(this);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "There was a problem loading the app manifest.", Toast.LENGTH_LONG).show();
            _paidVersion = true;
        }

        _bufferVisuals = new BufferVisualsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.upperFragmentContainer, new ExpressionFragment())
                .add(R.id.mainBufferFragmentContainer, _bufferVisuals)
                .add(R.id.mainParametersFragmentContainer, new ParametersFragment())
                .add(R.id.mainSelectorFragmentContainer, new ExpressionListFragment())
                .add(R.id.mainKeyboardFragmentContainer, new KeyboardFragment())
                .commit();

        _expressionTitleTextView = (TextView)findViewById(R.id.expressionTitleTextView);

        _keyboardContainer = findViewById(R.id.mainKeyboardFragmentContainer);
        _selectorContainer = findViewById(R.id.mainSelectorFragmentContainer);
        _paramsContainer = findViewById(R.id.mainParametersFragmentContainer);

        _loadNewExpressionBtn = findViewById(R.id.selectNewExpressionLayout);
        _startBtn = findViewById(R.id.mainStartBtn);
        _stopBtn = findViewById(R.id.mainStopBtn);
        _stopBtn.setVisibility(View.INVISIBLE);

        _recordBtn = findViewById(R.id.mainRecordButton);
        if(!_paidVersion){
            _recordBtn.setVisibility(View.GONE);
        }

        _copyBtn = findViewById(R.id.mainCopyButton);
        _pasteBtn = findViewById(R.id.mainPasteButton);
        _addBtn = findViewById(R.id.mainAddButton);

        _loadNewExpressionBtn.setOnClickListener(this);
        _startBtn.setOnClickListener(this);
        _stopBtn.setOnClickListener(this);
        _recordBtn.setOnClickListener(this);
        _copyBtn.setOnClickListener(this);
        _pasteBtn.setOnClickListener(this);
        _addBtn.setOnClickListener(this);

        _appController = getCompositionRoot().getAppController();
        ((AppController)_appController).setActivity(this);
        _appController.ShowParams();

        _repo = _root.getExpressionsRepository();
        ExpressionIO io = _root.getExpressionIO();
        try {
            io.loadAll();
        } catch (IOException e) {
            Message.err("Could not load saved expressions.");
        }

        _mediaControlsPresenter = _root.getMediaControlsPresenter();
        _mediaControlsPresenter.setView(this);

        _clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void onPause() {
        super.onPause();
        _mediaControlsPresenter.stop();
    }
    //endregion

    private void setPlaying(boolean playing){
        _startBtn.setVisibility (playing ? View.GONE : View.VISIBLE);
        _stopBtn.setVisibility  (playing ? View.VISIBLE : View.GONE);

        if(_paidVersion)
            _recordBtn.setVisibility(playing ? View.GONE : View.VISIBLE);
    }

    private void setRecording(boolean recording){
        _bufferVisuals.setRecordingText(recording);
    }

    //region OnClickListener methods
    @Override
    public void onClick(View arg0) {
        if (arg0 == _stopBtn) {
            boolean wasRecording = _mediaControlsPresenter.isRecording();
            setPlaying(false);
            setRecording(false);
            _mediaControlsPresenter.stop();
            if(wasRecording)
                showExportDialog(_mediaControlsPresenter.getRecordingPath());

        }else if (arg0 == _startBtn) {
            setPlaying(true);
            _mediaControlsPresenter.startPlay();
        } else if(arg0 == _recordBtn) {
            try {
                _mediaControlsPresenter.startRecord();
                setPlaying(true);
                setRecording(true);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to save to file.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if(arg0 == _loadNewExpressionBtn){
            _appController.ShowSelector();
        } else if(arg0 == _addBtn){
            new CreateExpressionFragment()
                .show(getSupportFragmentManager(), "NamingDialog");
        } else if(arg0 == _copyBtn){
            _clipboard.setText(_repo.getActive().getExpressionString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        } else if(arg0 == _pasteBtn){
            new CreateExpressionFragment()
                .setExpressionToCreate((String) _clipboard.getText())
                .show(getSupportFragmentManager(), "NamingDialog");
        }
    }

    private void showExportDialog(String recordingPath) {
        ExportFragment frag = new ExportFragment();
        frag.setSavedPath(recordingPath);
        frag.show(getSupportFragmentManager(), "ExportFragment");
    }
    //endregion

    //region IMediaControlsView methods

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

    @Override
    public void onBackPressed() {
        if(!_repo.hasDirty()) {
            finish();
            return;
        }
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Unsaved Expressions")
            .setMessage("Are you sure you want to exit and lose unsaved changes?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    //endregion
}
