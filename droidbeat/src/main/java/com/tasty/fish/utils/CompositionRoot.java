package com.tasty.fish.utils;

import com.tasty.fish.android.AppController;
import com.tasty.fish.android.media.audio.AudioPlayer;
import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ExpressionEvaluator;
import com.tasty.fish.domain.implementation.ExpressionsRepository;
import com.tasty.fish.presenters.BufferVisualsPresenter;
import com.tasty.fish.presenters.CreateExpressionPresenter;
import com.tasty.fish.presenters.ExpressionIO;
import com.tasty.fish.presenters.ExpressionPresenter;
import com.tasty.fish.presenters.ExpressionSelectionPresenter;
import com.tasty.fish.presenters.MediaControlsPresenter;
import com.tasty.fish.presenters.ParametersPresenter;
import com.tasty.fish.views.IAppController;

public class CompositionRoot {

    private ExpressionSelectionPresenter _expressionSelectorPresenter;
    private IExpressionEvaluator _expressionEvaluator;
    private IExpressionsRepository _expressionsRepository;
    private MediaControlsPresenter _mediaControlsPresenter;
    private IAudioPlayer _audioPlayer;
    private BufferVisualsPresenter _bufferVisualsPresenter;
    private ParametersPresenter _parametersPresenter;
    private ExpressionPresenter _expressionPresenter;
    private AppController _appController;
    private ExpressionIO _expressionIO;

    public ExpressionSelectionPresenter getExpressionSelectorPresenter() {
        return _expressionSelectorPresenter != null ?
               _expressionSelectorPresenter :
              (_expressionSelectorPresenter = new ExpressionSelectionPresenter(
                      getExpressionIO(),
                      getExpressionsRepository(),
                      getAppController()
              ));
    }

    public ExpressionIO getExpressionIO(){
        return _expressionIO != null ?
                _expressionIO : new ExpressionIO(new FileSystem(), getExpressionsRepository());
    }

    public IExpressionEvaluator getExpressionEvaluator() {
        return _expressionEvaluator != null ?
               _expressionEvaluator :
              (_expressionEvaluator = new ExpressionEvaluator());
    }

    public IExpressionsRepository getExpressionsRepository() {
        return _expressionsRepository != null ?
               _expressionsRepository :
              (_expressionsRepository = new ExpressionsRepository());
    }

    public MediaControlsPresenter getMediaControlsPresenter() {
        return
            _mediaControlsPresenter != null ?
            _mediaControlsPresenter :
           (_mediaControlsPresenter = new MediaControlsPresenter(
                getExpressionEvaluator(),
                getExpressionsRepository(),
                getAudioPlayer(),
                getBufferVisualsPresenter(),
                getAppController()
            ));
    }

    public IAudioPlayer getAudioPlayer() {
        return
            _audioPlayer != null ?
            _audioPlayer :
            (_audioPlayer = new AudioPlayer(
                getExpressionEvaluator()
            ));
    }

    public BufferVisualsPresenter getBufferVisualsPresenter() {
        return  _bufferVisualsPresenter != null ?
                _bufferVisualsPresenter :
               (_bufferVisualsPresenter =
                   new BufferVisualsPresenter(
                           getParametersPresenter(),
                           getExpressionEvaluator()));
    }

    public ParametersPresenter getParametersPresenter() {
        return  _parametersPresenter != null ?
                _parametersPresenter :
               (_parametersPresenter = new ParametersPresenter(
                   getExpressionsRepository(),
                   getExpressionEvaluator()
               ));
    }

    public ExpressionPresenter getExpressionPresenter() {
        return _expressionPresenter != null ?
               _expressionPresenter :
              (_expressionPresenter = new ExpressionPresenter(
                  getExpressionEvaluator(),
                  getExpressionsRepository(),
                  getAppController()
              ));
    }

    public IAppController getAppController() {
        return _appController != null ?
               _appController :
              (_appController = new AppController());
    }

    public CreateExpressionPresenter getCreateExpressionPresenter() {
        return new CreateExpressionPresenter(
                getExpressionsRepository()
        );
    }
}
