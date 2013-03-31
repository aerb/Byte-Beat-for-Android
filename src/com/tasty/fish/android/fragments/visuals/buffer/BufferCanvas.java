package com.tasty.fish.android.fragments.visuals.buffer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BufferCanvas extends View {

    private static final int MAX = 256;
    byte _buffer[] = null;
    private Paint _paint;
    private int _drawableLength;

    public BufferCanvas(Context context) {
        super(context);
        initialize();
    }

    public BufferCanvas(Context context, AttributeSet attrs) {
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

        for (int i = 0; i < _drawableLength; ++i) {
            float y = h - h * (float)(_buffer[i] + 128) /  256;
            float y1 = h - h * (float)(_buffer[i + 1] + 128) /  256;

            float x = ((float) i) /  _drawableLength * w ;
            float x1 = ((float) i + 1) / _drawableLength * w;

            //c.drawLine(x, y, x1, y1, _paint);
            c.drawRect(
                    x,
                    y > y1 ? y : y1,
                    x1,
                    (float)h,
                    _paint);
        }
    }
}
