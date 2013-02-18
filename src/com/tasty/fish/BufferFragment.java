package com.tasty.fish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BufferFragment extends Fragment implements IBufferView {
    private BufferCanvas _bufferCanvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _bufferCanvas = (BufferCanvas) inflater.inflate(R.id.bufferView, null);

        ((DroidBeatView)getActivity()).getDroidbeatPresenter().setBufferView(this);

        return _bufferCanvas;
    }

    @Override
    public void registerIBufferViewListener(IBufferView listener) {
        //don't really care right now.
    }

    public void setDisplayBuffer(byte[] samples, int t) {
        _bufferCanvas.setSamples(samples);
        _bufferCanvas.updateT(t);
        _bufferCanvas.postInvalidate();
    }

    public void setTime(int time) {
    }
}
