package com.tasty.fish.presenters;

import com.tasty.fish.android.media.audio.IAudioPlayer;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.parser.ExpressionParsingException;
import com.tasty.fish.views.IBufferView;
import com.tasty.fish.views.IDroidBeatView;
import com.tasty.fish.views.IParameterView;

public class DroidBeatPresenter implements
        IDroidBeatView.IDroidBeatViewListener,
        IExpressionsRepository.IExpressionsRepositoryListener
{
    private IDroidBeatView _view;

    private ByteBeatExpression _activeExpression = null;

    private final IExpressionEvaluator _evaluator;
    private final IExpressionsRepository _repo;


    public DroidBeatPresenter(
            IDroidBeatView view,
            IExpressionEvaluator evaluator,
            IExpressionsRepository repo,
            IAudioPlayer _audio,
            BufferVisualsPresenter _bufferPresenter
    )
    {
        _view = view;
        _evaluator = evaluator;
        _repo = repo;
        _repo.setIExpressionsRepositoryListener(this);
    }


    private void updateView() {
        _view.setTitle(_activeExpression.getName());
    }


    //region IDroidBeatViewListener methods
    @Override
    public void OnStartPlay() {
        //startAudioThread();
        //startVideoThread();
    }

    @Override
    public void OnStopPlay() {
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
