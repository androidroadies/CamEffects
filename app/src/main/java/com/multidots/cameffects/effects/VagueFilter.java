package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Color;

public class VagueFilter {
	// æ¨¡ç³Šæ•ˆæžœå‡½æ•°
	public static Bitmap changeToVague(Bitmap bitmap) {
		return changeToVague(bitmap, 10);
	}
		
	// æ¨¡ç³Šæ•ˆæžœå‡½æ•°ï¼ŒvagueDegreeä¸ºæ¨¡ç³Šåº¦
	public static Bitmap changeToVague(Bitmap bitmap, int vagueDegree) {
		int width, height;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
			
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixels[] = new int[width * height];
		int pixelsRawSource[] = new int[width * height * 3];
		int pixelsRawNew[] = new int[width * height * 3];
			
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
			
		for (int i = 1; i <= vagueDegree; i++) {
			for (int j = 0; j < pixels.length; j++) {
				pixelsRawSource[j * 3 + 0] = Color.red(pixels[j]);
				pixelsRawSource[j * 3 + 1] = Color.green(pixels[j]);
				pixelsRawSource[j * 3 + 2] = Color.blue(pixels[j]);
			}
				
			int currentPixel = width * 3 + 3;
				
			for (int j = 0; j < height - 3; j++) {
				for (int k = 0; k < width * 3; k++) {
					currentPixel += 1;
					int sumColor = 0;
					sumColor = pixelsRawSource[currentPixel - width * 3];
					sumColor = sumColor + pixelsRawSource[currentPixel - 3]; // å·¦ä¸€ç‚¹                 
	                sumColor = sumColor + pixelsRawSource[currentPixel + 3]; // å�³ä¸€ç‚¹                 
	                sumColor = sumColor + pixelsRawSource[currentPixel + width * 3]; // ä¸‹ä¸€ç‚¹                 
	                pixelsRawNew[currentPixel] = Math.round(sumColor / 4); // è®¾ç½®åƒ�ç´ ç‚¹             
				}
			}
				
			for (int j = 0; j < pixels.length; j++) {
				pixels[j] = Color.rgb(pixelsRawNew[j * 3 + 0], pixelsRawNew[j * 3 + 1], pixelsRawNew[j * 3 + 2]);
			}
		}
			
		returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			
		return returnBitmap;
	}
}
