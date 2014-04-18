package com.tasty.fish.presenters;

import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;
import com.tasty.fish.views.IAppController;
import com.tasty.fish.views.IMediaControlsView;

public class MediaControlsPresenter implements
        IMediaControlsView.IMediaControlsListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{
    private IMediaControlsView _view;

    private final IExpressionEvaluator _evaluator;
    private final IExpressionsRepository _repo;
    private final IAudioPlayer _audio;
    private final BufferVisualsPresenter _visuals;
    private final IAppController _appController;

    public MediaControlsPresenter(
            IExpressionEvaluator evaluator,
            IExpressionsRepository repo,
            IAudioPlayer audio,
            BufferVisualsPresenter visuals,
            IAppController appController
    )
    {
        _evaluator = evaluator;

        _repo = repo;
        _repo.setIExpressionsRepositoryListener(this);

        try {
            _evaluator.setExpression(_repo.getActive());
        } catch (ExpressionParsingException e) {
            e.printStackTrace();
        }

        _audio = audio;

        _visuals = visuals;
        _visuals.setBuffer(_audio.getBuffer());

        _appController = appController;
    }

    private void updateView() {
        _view.setTitle(_repo.getActive().getName());
    }

    public void setView(IMediaControlsView view) {
        _view = view;
        updateView();
    }

    //region IMediaControlsListener methods
    @Override
    public void OnStartPlay() {
        _audio.start();
        _visuals.start();
    }

    @Override
    public void OnStopPlay() {
        _audio.stop();
        _visuals.stop();
    }
    //endregion

    //region IExpressionRepository methods
    @Override
    public void OnActiveExpressionChanged() {
        ByteBeatExpression exp = _repo.getActive();
        try {
            _evaluator.setExpression(exp);
        } catch (ExpressionParsingException e) {
            e.printStackTrace();
        }
        updateView();
    }
    //endregion
}
