package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

@SuppressWarnings("unused")
public class WaterMaking {

	public static Bitmap mark(Bitmap src, String watermark, Point location, int color, int alpha, int size, boolean underline) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAlpha(alpha);
		paint.setTextSize(size);
		paint.setAntiAlias(true);
		paint.setUnderlineText(underline);
		canvas.drawText(watermark, location.x, location.y, paint);

		return result;
	}
}
