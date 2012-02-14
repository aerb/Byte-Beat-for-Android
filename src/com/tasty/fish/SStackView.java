package com.tasty.fish;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class SStackView extends View {

	byte samples[] = null;
	int t = 0;
	
	public SStackView(Context context) {
		super(context);
	}

	public SStackView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSamples(byte[] samples) {
		this.samples = samples;
	}

	public void updateT(int t) {
		this.t = t;
	}

	@Override
	protected void onDraw(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setStrokeWidth(5);
		p.setTextSize(30);
		if (samples == null)
			return;

		int h = this.getHeight();
		int w = this.getWidth();

		for (int i = 0; i < samples.length / 4; ++i) {
			float y = h - (float) h * ((float) samples[i]) / (float) 255 * 2;
			float y1 = h - (float) h * ((float) samples[i + 1]) / (float) 255
					* 2;
			float x = ((float) i) / ((float) samples.length) * w * 4;
			float x1 = ((float) i + 1) / ((float) samples.length) * w * 4;
			c.drawLine(x, y, x1, y1, p);
		}
		
		c.drawText(String.format("%d", t), 10, 30, p);
	}

}
