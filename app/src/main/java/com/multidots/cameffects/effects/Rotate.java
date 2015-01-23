package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Rotate {

	public static Bitmap rotate(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);

		// return new bitmap rotated using matrix
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}
}
