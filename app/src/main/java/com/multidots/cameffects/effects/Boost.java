package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Boost {
	public static Bitmap boost(Bitmap src, int type, float percent) {
		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		int A, R, G, B;
		int pixel;

		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				if(type == 1) {
					R = (int)(R * (1 + percent));
					if(R > 255) R = 255;
				}
				else if(type == 2) {
					G = (int)(G * (1 + percent));
					if(G > 255) G = 255;
				}
				else if(type == 3) {
					B = (int)(B * (1 + percent));
					if(B > 255) B = 255;
				}
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return bmOut;
	}

}
