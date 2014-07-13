package com.tasty.fish.presenters;

import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;
import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.utils.FileSystem;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IMediaControlsView;

import java.io.IOException;

public class MediaControlsPresenter
{
    private IMediaControlsView _view;

    private final IExpressionEvaluator _evaluator;
    private final IExpressionList _repo;
    private final IAudioPlayer _audio;
    private final BufferVisualsPresenter _visuals;
    private boolean _recording;
    private String _recordingPath;

    public MediaControlsPresenter(
            IExpressionEvaluator evaluator,
            IExpressionList repo,
            IAudioPlayer audio,
            BufferVisualsPresenter visuals,
            IAppController appController
    )
    {
        _evaluator = evaluator;
        _repo = repo;
        _evaluator.setExpression(_repo.getActive());
        _audio = audio;
        _visuals = visuals;
        _visuals.setBuffer(_audio.getBuffer());
    }

    private void updateView() {
        _view.setTitle(_repo.getActive().getName());
    }

    public void setView(IMediaControlsView view) {
        _view = view;
        _repo.addActiveChangedListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression expression) {
                _evaluator.setExpression(expression);
                updateView();
            }
        });
        updateView();
    }

    public void startRecord() throws IOException {
        String path = new FileSystem().getNextExportName();
        _audio.startAndRecord(path);
        _visuals.start();
        _recording = true;
        _recordingPath = path;
    }

    public void startPlay() {
        _audio.start();
        _visuals.start();
        _recording = false;
    }

    public boolean isRecording(){
        return _recording;
    }

    public void stop() {
        _recording = false;
        _audio.stop();
        _visuals.stop();
    }

    public String getRecordingPath() {
        return _recordingPath;
    }
    //endregion
}
