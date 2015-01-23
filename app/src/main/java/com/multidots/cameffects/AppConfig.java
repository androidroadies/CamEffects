package com.multidots.cameffects;

import android.graphics.Bitmap;

public class AppConfig {

//	public static Bitmap original_bitmap;
//	
//	public static Bitmap bitmapBackUp, bitmapEffect, bitmapTemp;
//	public static Bitmap mBitmap;
//	
	

	//	public static Bitmap bitmapCrop, bitmapEffect;
	public static Bitmap /*mBitmapOriginal,*/bitmapTemp/*bitmapEffect,*//*MainOriginalImage*/;
	
	public static Bitmap mOrigenalBitmap, mEffectedBitmap, mTempBitmap;
	public static int fixedWidth=0;
	public static int fixedHeight=0;
	
	public static boolean isOrigenal = true;
	public static float scale=1;
	
	public static Bitmap img_change_to_gray_bitmap;
	public static Bitmap img_change_to_oil_bitmap;
	public static Bitmap img_change_to_relief_bitmap;
	public static Bitmap img_change_to_vague_bitmap;
	public static Bitmap img_change_to_neon_bitmap;
	public static Bitmap img_change_to_pixelate_bitmap;
	public static Bitmap img_change_to_invert_bitmap;
	public static Bitmap img_change_to_tv_bitmap;
	public static Bitmap img_fly_bitmap;
	public static Bitmap img_contrast_bitmap;
	public static Bitmap img_change_to_block_bitmap;
	public static Bitmap img_change_to_old_bitmap;
	public static Bitmap img_change_to_sharpen_bitmap;
	public static Bitmap img_change_to_light_bitmap;
	public static Bitmap img_photography_bitmap;
	public static Bitmap img_decrising_bitmap;
	public static Bitmap img_rotate_bitmap;
	public static Bitmap img_brightness_bitmap;
	public static Bitmap img_blur_bitmap;
	public static Bitmap img_roundcorner_bitmap;
	public static Bitmap img_boost_bitmap;
	public static Bitmap img_light_bitmap;
	
	
	public static boolean isCrop = false,isStatus = true,BitmapStatus = false,isImageSave=true;
	
	public static String imgPath,myText="myText";

	protected static String imageName;

	public static boolean isFxEffect = true;
	
}