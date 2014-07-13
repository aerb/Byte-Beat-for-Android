package com.tasty.fish.android.fragments.visuals.buffer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tasty.fish.domain.Listener;
import com.tasty.fish.presenters.MediaController;

import java.util.Timer;
import java.util.TimerTask;

public class AudioBufferView extends View {

    private static final int MAX = 256;
    byte _buffer[] = null;
    private Paint _paint;
    private int _drawableLength;
    private Timer _updateTimer = new Timer();

    public AudioBufferView(Context context) {
        super(context);
        initialize();
    }

    public AudioBufferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void setDisplayBuffer(byte[] buffer) {
        _buffer = buffer;
        _drawableLength = _buffer.length > MAX ?
                MAX :
                _buffer.length;
    }

    private void initialize() {
        _paint = new Paint();
        _paint.setColor(0xDD5FCCDD);
        _paint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas c) {
        if (_buffer == null ||
            _paint == null)
            return;

        int h = getHeight();
        int w = getWidth();

        for (int i = 0; i < _drawableLength - 1; ++i) {
            float y = h - h * (float)(_buffer[i] + 128) /  256;
            float y1 = h - h * (float)(_buffer[i + 1] + 128) /  256;

            float x = ((float) i) /  _drawableLength * w ;
            float x1 = ((float) i + 1) / _drawableLength * w;

            c.drawRect(
                    x,
                    y > y1 ? y : y1,
                    x1,
                    (float)h,
                    _paint);
        }
    }

    public void setMediaController(final MediaController mediaController) {
        mediaController.addStartListener(new Listener<MediaController>() {
            @Override
            public void onEvent(final MediaController item) {
                setDisplayBuffer(mediaController.getAudio().getBuffer());
                _updateTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        postInvalidate();
                    }
                }, 0, 100);
            }
        });
        mediaController.addStopListener(new Listener<MediaController>() {
            @Override
            public void onEvent(MediaController item) {
                _updateTimer.cancel();
                _updateTimer = new Timer();
            }
        });
    }
}
