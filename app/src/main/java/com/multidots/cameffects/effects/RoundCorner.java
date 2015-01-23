package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundCorner {
	public static Bitmap roundCorner(Bitmap src, float round) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create bitmap output
	    Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    // set canvas for painting
	    Canvas canvas = new Canvas(result);
	    canvas.drawARGB(0, 0, 0, 0);

	    // config paint
	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLACK);

	    // config rectangle for embedding
	    final Rect rect = new Rect(0, 0, width, height);
	    final RectF rectF = new RectF(rect);

	    // draw rect to canvas
	    canvas.drawRoundRect(rectF, round, round, paint);

	    // create Xfer mode
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    // draw source image to canvas
	    canvas.drawBitmap(src, rect, rect, paint);

	    // return final image
	    return result;
	}
}
