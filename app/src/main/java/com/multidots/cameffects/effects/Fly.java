package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Fly {
	public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
		// create output image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// color information
		int A, R, G, B;
		int pixel;
		// constant value curve
		final int    MAX_SIZE = 256;
		final double MAX_VALUE_DBL = 255.0;
		final int    MAX_VALUE_INT = 255;
		final double REVERSE = 1.0;

		// gamma arrays
		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		// setting values for every gamma channels
		for(int i = 0; i < MAX_SIZE; ++i) {
			gammaR[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.6));
			gammaG[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.6));
			gammaB[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.6));
		}

		// apply gamma table
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// look up gamma
				R = gammaR[Color.red(pixel)];
				G = gammaG[Color.green(pixel)];
				B = gammaB[Color.blue(pixel)];
				// set new color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}
}
