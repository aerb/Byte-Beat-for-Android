package com.tasty.fish.android.fragments.visuals.buffer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.tasty.fish.android.DroidBeatActivity;
import com.tasty.fish.R;
import com.tasty.fish.presenters.BufferVisualsPresenter;
import com.tasty.fish.views.IBufferView;

public class BufferVisualsFragment extends Fragment implements IBufferView {
    private LinearLayout _layout;
    private BufferCanvas _canvas;
    private BufferVisualsPresenter _presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _layout = (LinearLayout) inflater.inflate(R.layout.buffer_display, null);
        _canvas = (BufferCanvas)_layout.findViewById(R.id.bufferCanvas);

        _presenter =
            ((DroidBeatActivity)getActivity())
            .getCompositionRoot()
            .getBufferVisualsPresenter();

        _presenter.setView(this);
        return _layout;
    }

    @Override
    public void registerIBufferViewListener(IBufferView listener) {
        //don't really care right now.
}

    public void setDisplayBuffer(byte[] samples, int t) {
        _canvas.setSamples(samples);
        _canvas.updateT(t);
        _canvas.postInvalidate();
    }

    public void setTime(int time) {
    }
}
