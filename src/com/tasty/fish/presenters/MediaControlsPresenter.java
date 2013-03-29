package com.tasty.fish.presenters;

import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.ExpressionParsingException;
import com.tasty.fish.views.IMediaControlsView;

public class MediaControlsPresenter implements
        IMediaControlsView.IMediaControlsListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{
    private IMediaControlsView _view;

    private ByteBeatExpression _activeExpression = null;

    private final IExpressionEvaluator _evaluator;
    private final IExpressionsRepository _repo;
    private final IAudioPlayer _audio;


    public MediaControlsPresenter(
            IExpressionEvaluator evaluator,
            IExpressionsRepository repo,
            IAudioPlayer audio
    )
    {
        _evaluator = evaluator;
        _repo = repo;
        _repo.setIExpressionsRepositoryListener(this);
        _audio = audio;
    }

    private void updateView() {
        _view.setTitle(_activeExpression.getName());
    }

    public void setView(IMediaControlsView view) {
        _view = view;
    }

    //region IMediaControlsListener methods
    @Override
    public void OnStartPlay() {
        _audio.start();
    }

    @Override
    public void OnStopPlay() {
        _audio.stop();
    }
    //endregion

    //region IExpressionRepository methods
    @Override
    public void OnActiveExpressionChanged() {
        ByteBeatExpression exp = _repo.getActive();
        try {
            _evaluator.setExpression(exp);
        } catch (ExpressionParsingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        _activeExpression = exp;
        updateView();
    }
    //endregion
}
