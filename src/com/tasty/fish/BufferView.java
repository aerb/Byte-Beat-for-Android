package com.tasty.fish;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BufferView extends View {

	byte samples[] = null;
	int t = 0;
	int scaleamount = 4;
	String eq = "";

	public BufferView(Context context) {
		super(context);
	}

	public BufferView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSamples(byte[] samples) {
		this.samples = samples;
	}

	public void updateT(int t) {
		this.t = t;
	}

	public void updateEq(String c) {
		this.eq = c;
	}

	@Override
	protected void onDraw(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setStrokeWidth(5);
		p.setTextSize(30);

		Paint p2 = new Paint();
		p2.setColor(Color.WHITE);
		p2.setStrokeWidth(5);
		p2.setTextSize(20);

		if (samples == null)
			return;

		int h = this.getHeight();
		int w = this.getWidth();

		for (int i = 0; i < (samples.length - 1) / scaleamount; ++i) {
			float y = h - (float) h * ((float) samples[i]) / (float) 255 * 2;
			float y1 = h - (float) h * ((float) samples[i + 1]) / (float) 255
					* 2;
			float x = ((float) i) / ((float) samples.length) * w * scaleamount;
			float x1 = ((float) i + 1) / ((float) samples.length) * w
					* scaleamount;
			c.drawLine(x, y, x1, y1, p);
		}

		/*
		int scaleFactor = 10;
		Paint p0 = new Paint();
		p0.setStrokeWidth(scaleFactor);
		int bgHeight = 64, bgWidth = 64;		
		for (int x = 0; x < bgWidth; ++x) {
			for (int y = 0; y < bgHeight; ++y) {
				int sampleIndex = 0;
				sampleIndex = y + x * bgWidth;
				// val = y;
				if (sampleIndex >= samples.length) continue;
				int val = 0x000000FF & samples[sampleIndex];
				p0.setColor(0xFF000000 | (val << 16) | (val << 8) | (val));
				c.drawPoint(x * scaleFactor, y * scaleFactor, p0);
			}
		}
		*/
		//--bgHeight;
		//c.drawText(String.format("%d", bgHeight), 10, 30, p);
	}
}
