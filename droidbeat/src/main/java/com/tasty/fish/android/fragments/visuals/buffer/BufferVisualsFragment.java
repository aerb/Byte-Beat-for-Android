package com.tasty.fish.android.fragments.visuals.buffer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasty.fish.R;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.domain.IExpressionEvaluator;
import com.tasty.fish.domain.IExpressionList;
import com.tasty.fish.presenters.MediaController;


public class BufferVisualsFragment extends Fragment {
    private TextView _performanceText;
    private View _recordingText;
    private IExpressionList _expressions;
    private IExpressionEvaluator _evaluator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.buffer_display, null);
        if(layout == null) throw new IllegalArgumentException();

        _performanceText = (TextView) layout.findViewById(R.id.performanceText);
        _performanceText.setVisibility(View.GONE);

        _recordingText = layout.findViewById(R.id.recordingText);
        _recordingText.setVisibility(View.INVISIBLE);

        MediaController _mediaController = ((DroidBeatActivity) getActivity())
                .getCompositionRoot()
                .getMediaController();
        _expressions =
            ((DroidBeatActivity) getActivity())
            .getCompositionRoot()
            .getExpressionsRepository();
        _evaluator =
            ((DroidBeatActivity) getActivity())
            .getCompositionRoot()
            .getExpressionEvaluator();

        View _resetT = layout.findViewById(R.id.resetT);
        _resetT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _evaluator.resetTime();
            }
        });

        View _resetArgs = layout.findViewById(R.id.resetArgs);
        _resetArgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _expressions.getActive().resetParametersAndTimeDelta();
            }
        });

        AudioBufferView bufferView = (AudioBufferView) layout.findViewById(R.id.bufferCanvas);
        bufferView.setMediaController(_mediaController);
        return layout;
    }

    public void setPerformanceText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _performanceText.setText(text);
            }
        });
    }

    public void setRecordingText(boolean showing) {
        _recordingText.setVisibility(showing ? View.VISIBLE : View.INVISIBLE);
    }
}
