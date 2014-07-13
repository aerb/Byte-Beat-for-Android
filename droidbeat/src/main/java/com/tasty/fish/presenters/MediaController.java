package com.tasty.fish.presenters;

import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.domain.Listener;
import com.tasty.fish.domain.implementation.Expression;
import com.tasty.fish.domain.implementation.ListenerSet;
import com.tasty.fish.utils.FileSystem;
import com.tasty.fish.views.IAppController;

import java.io.IOException;

public class MediaController
{
    private final IExpressionEvaluator _evaluator;
    private final IExpressionList _repo;
    private final IAudioPlayer _audio;
    private boolean _recording;
    private String _recordingPath;
    private ListenerSet<MediaController> _startListener = new ListenerSet<MediaController>();
    private ListenerSet<MediaController> _stopListener = new ListenerSet<MediaController>();

    public MediaController(
            IExpressionEvaluator evaluator,
            IExpressionList repo,
            IAudioPlayer audio,
            IAppController appController
    )
    {
        _evaluator = evaluator;
        _repo = repo;
        _evaluator.setExpression(_repo.getActive());
        _audio = audio;
        _repo.addActiveChangedListener(new Listener<Expression>() {
            @Override
            public void onEvent(Expression item) {
                _evaluator.setExpression(item);
            }
        });
    }

    public void addStartListener(Listener<MediaController> listener){
        _startListener.add(listener);
    }

    public void addStopListener(Listener<MediaController> listener){
        _stopListener.add(listener);
    }

    public void startRecord() throws IOException {
        String path = new FileSystem().getNextExportName();
        _audio.startAndRecord(path);
        _startListener.notify(this);
        _recording = true;
        _recordingPath = path;
    }

    public void startPlay() {
        _audio.start();
        _startListener.notify(this);
        _recording = false;
    }

    public boolean isRecording(){
        return _recording;
    }

    public void stop() {
        _recording = false;
        _audio.stop();
        _stopListener.notify(this);
    }

    public String getRecordingPath() {
        return _recordingPath;
    }

    public IAudioPlayer getAudio() {
        return _audio;
    }
}
