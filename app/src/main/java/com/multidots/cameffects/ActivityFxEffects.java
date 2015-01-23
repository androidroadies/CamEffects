package com.multidots.cameffects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.multidots.cameffects.effects.BitmapFilter;

public class ActivityFxEffects extends Activity implements OnClickListener {
	Context context;
	Bitmap sourceBitmap;
	ImageView img_change_to_relief, img_change_to_vague, img_change_to_oil,
			img_light, img_change_to_gray;
	ImageView img_change_to_neon, img_change_to_pixelate, img_change_to_invert,
			img_change_to_tv, img_fly, img_contrast;
	ImageView img_change_to_block, img_change_to_old, img_change_to_sharpen,
			img_change_to_light, img_photography;
	ImageView img_decrising, img_rotate, img_brightness;// img_matrix,img_water
	ImageView img_boost, img_round, img_blur;
	ImageView ivImagea, iv_effect_save, iv_cross;
	LinearLayout ll_effects;
	HorizontalScrollView hl_view;
	Intent intent;
	Bitmap bitmap;
	String currentDateandTime, imgType = ".png";

	// RelativeLayout ll_effects123;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fx_effect);

		System.gc();

		if (AppConfig.isOrigenal)
			sourceBitmap = AppConfig.mOrigenalBitmap;
		else
			sourceBitmap = AppConfig.mEffectedBitmap;

		context = ActivityFxEffects.this;

		initialization();

		// AppConfig.bitmapEffect = Bitmap.createBitmap(AppConfig.bitmapEffect);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int squaresize = dm.widthPixels;

		ivImagea.getLayoutParams().width = squaresize;
		ivImagea.getLayoutParams().height = squaresize;

		ivImagea.setImageBitmap(sourceBitmap);

		if (AppConfig.isFxEffect == true) {
			MyTask runner = new MyTask();
			runner.execute();
			AppConfig.isFxEffect = false;
		} else {
			hl_view.setVisibility(View.VISIBLE);
			hl_view.startAnimation(AnimationUtils.loadAnimation(
					ActivityFxEffects.this, R.anim.slide_right_to_left));

			// 1
			img_change_to_gray
					.setImageBitmap(AppConfig.img_change_to_gray_bitmap);
			// 2
			img_change_to_relief
					.setImageBitmap(AppConfig.img_change_to_relief_bitmap);
			// 3
			img_change_to_vague
					.setImageBitmap(AppConfig.img_change_to_vague_bitmap);

			// 4
			img_change_to_oil
					.setImageBitmap(AppConfig.img_change_to_oil_bitmap);

			// 5
			img_light.setImageBitmap(AppConfig.img_light_bitmap);
			// 6
			img_change_to_neon
					.setImageBitmap(AppConfig.img_change_to_neon_bitmap);

			// 7
			img_change_to_pixelate
					.setImageBitmap(AppConfig.img_change_to_pixelate_bitmap);

			// 8
			img_change_to_invert
					.setImageBitmap(AppConfig.img_change_to_invert_bitmap);

			// 9
			img_change_to_tv.setImageBitmap(AppConfig.img_change_to_tv_bitmap);

			// 10
			img_fly.setImageBitmap(AppConfig.img_fly_bitmap);

			// 11
			img_contrast.setImageBitmap(AppConfig.img_contrast_bitmap);

			// 12
			img_change_to_block
					.setImageBitmap(AppConfig.img_change_to_block_bitmap);

			// 13
			img_change_to_old
					.setImageBitmap(AppConfig.img_change_to_old_bitmap);

			// 14
			img_change_to_sharpen
					.setImageBitmap(AppConfig.img_change_to_sharpen_bitmap);

			// 15
			img_change_to_light
					.setImageBitmap(AppConfig.img_change_to_light_bitmap);

			// 16
			img_photography.setImageBitmap(AppConfig.img_photography_bitmap);

			// 17
			img_decrising.setImageBitmap(AppConfig.img_decrising_bitmap);

			// 18
			img_rotate.setImageBitmap(AppConfig.img_rotate_bitmap);

			// 19
			img_brightness.setImageBitmap(AppConfig.img_brightness_bitmap);

			// 20
			img_blur.setImageBitmap(AppConfig.img_blur_bitmap);

			// 21
			img_round.setImageBitmap(AppConfig.img_roundcorner_bitmap);

			// 22
			img_boost.setImageBitmap(AppConfig.img_boost_bitmap);

			System.gc();
		}

		img_change_to_gray.setOnClickListener(this);
		img_change_to_vague.setOnClickListener(this);
		img_change_to_relief.setOnClickListener(this);
		img_change_to_oil.setOnClickListener(this);
		img_light.setOnClickListener(this);
		img_change_to_neon.setOnClickListener(this);
		img_change_to_pixelate.setOnClickListener(this);
		img_change_to_invert.setOnClickListener(this);
		img_change_to_tv.setOnClickListener(this);
		img_fly.setOnClickListener(this);
		img_contrast.setOnClickListener(this);

		img_change_to_block.setOnClickListener(this);
		img_change_to_old.setOnClickListener(this);
		img_change_to_sharpen.setOnClickListener(this);
		img_change_to_light.setOnClickListener(this);
		img_photography.setOnClickListener(this);

		img_decrising.setOnClickListener(this);
		img_rotate.setOnClickListener(this);
		img_brightness.setOnClickListener(this);

		img_boost.setOnClickListener(this);
		img_round.setOnClickListener(this);
		img_blur.setOnClickListener(this);

		iv_effect_save.setOnClickListener(this);
		iv_cross.setOnClickListener(this);

	}

	private class MyTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog = new ProgressDialog(context);

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// ll_effects123.setVisibility(View.GONE);
			progressDialog.dismiss();
			hl_view.setVisibility(View.VISIBLE);
			hl_view.startAnimation(AnimationUtils.loadAnimation(
					ActivityFxEffects.this, R.anim.slide_right_to_left));
			System.gc();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ll_cancle_container.setEnabled(false);
			// ll_save_container.setEnabled(false);
			//
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// System.out.println("hel" + AppConfig.img_change_to_gray_bitmap);
			// if (AppConfig.img_change_to_gray_bitmap == null) {
			// System.out.println("heldddd"
			// + AppConfig.img_change_to_gray_bitmap);
			// } else {
			// System.out.println("hel000000"
			// + AppConfig.img_change_to_gray_bitmap);
			// }
			// effectiveThumbnails();

			Bitmap gray = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.GRAY_STYLE);
			AppConfig.img_change_to_gray_bitmap = gray;

			img_change_to_gray
					.setImageBitmap(AppConfig.img_change_to_gray_bitmap);

			Bitmap relief = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.RELIEF_STYLE);
			AppConfig.img_change_to_relief_bitmap = relief;
			img_change_to_relief
					.setImageBitmap(AppConfig.img_change_to_relief_bitmap);

			// 3
			Bitmap vague = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.VAGUE_STYLE);
			AppConfig.img_change_to_vague_bitmap = vague;
			img_change_to_vague
					.setImageBitmap(AppConfig.img_change_to_vague_bitmap);

			// 4
			Bitmap oil = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.OIL_STYLE);
			AppConfig.img_change_to_oil_bitmap = oil;
			img_change_to_oil
					.setImageBitmap(AppConfig.img_change_to_oil_bitmap);

			// 5
			Bitmap high = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.HIGHLIGHT);
			AppConfig.img_light_bitmap = high;
			img_light.setImageBitmap(AppConfig.img_light_bitmap);
			// 6
			Bitmap neon = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.NEON_STYLE);
			AppConfig.img_change_to_neon_bitmap = neon;
			img_change_to_neon
					.setImageBitmap(AppConfig.img_change_to_neon_bitmap);

			// 7
			Bitmap pixelate = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.PIXELATE_STYLE);
			AppConfig.img_change_to_pixelate_bitmap = pixelate;
			img_change_to_pixelate
					.setImageBitmap(AppConfig.img_change_to_pixelate_bitmap);

			// 8
			Bitmap invert = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.INVERT_STYLE);
			AppConfig.img_change_to_invert_bitmap = invert;
			img_change_to_invert
					.setImageBitmap(AppConfig.img_change_to_invert_bitmap);

			// 9
			Bitmap tv = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.TV_STYLE);
			AppConfig.img_change_to_tv_bitmap = tv;
			img_change_to_tv.setImageBitmap(AppConfig.img_change_to_tv_bitmap);

			// 10
			Bitmap fly = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.FLY);
			AppConfig.img_fly_bitmap = fly;
			img_fly.setImageBitmap(AppConfig.img_fly_bitmap);

			// 11
			Bitmap cBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.CONTRAST);
			AppConfig.img_contrast_bitmap = cBitmap;
			img_contrast.setImageBitmap(AppConfig.img_contrast_bitmap);

			// 12
			Bitmap brick = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.BLOCK_STYLE);
			AppConfig.img_change_to_block_bitmap = brick;
			img_change_to_block
					.setImageBitmap(AppConfig.img_change_to_block_bitmap);

			// 13
			Bitmap old = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.OLD_STYLE);
			AppConfig.img_change_to_old_bitmap = old;
			img_change_to_old
					.setImageBitmap(AppConfig.img_change_to_old_bitmap);

			// 14
			Bitmap sharpen = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.SHARPEN_STYLE);
			AppConfig.img_change_to_sharpen_bitmap = sharpen;
			img_change_to_sharpen
					.setImageBitmap(AppConfig.img_change_to_sharpen_bitmap);

			// 15
			Bitmap light = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.LIGHT_STYLE);
			AppConfig.img_change_to_light_bitmap = light;
			img_change_to_light
					.setImageBitmap(AppConfig.img_change_to_light_bitmap);

			// 16
			Bitmap pBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.PHOTOGRAPHY);
			AppConfig.img_photography_bitmap = pBitmap;
			img_photography.setImageBitmap(AppConfig.img_photography_bitmap);

			// 17
			Bitmap dBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.DECRISING);
			AppConfig.img_decrising_bitmap = dBitmap;
			img_decrising.setImageBitmap(AppConfig.img_decrising_bitmap);

			// 18
			Bitmap rBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.ROTATE);
			AppConfig.img_rotate_bitmap = rBitmap;
			img_rotate.setImageBitmap(AppConfig.img_rotate_bitmap);

			// 19
			Bitmap bright = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.BRIGHT);
			AppConfig.img_brightness_bitmap = bright;
			img_brightness.setImageBitmap(AppConfig.img_brightness_bitmap);

			// 20
			Bitmap blurBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.BLUR);
			AppConfig.img_blur_bitmap = blurBitmap;
			img_blur.setImageBitmap(AppConfig.img_blur_bitmap);

			// 21
			Bitmap rouBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.ROUND);
			AppConfig.img_roundcorner_bitmap = rouBitmap;
			img_round.setImageBitmap(AppConfig.img_roundcorner_bitmap);

			// 22
			Bitmap booBitmap = BitmapFilter.changeStyle(sourceBitmap,
					BitmapFilter.BOOST);
			AppConfig.img_boost_bitmap = booBitmap;
			img_boost.setImageBitmap(AppConfig.img_boost_bitmap);

			return null;
		}

	}

	// @Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.img_change_to_gray:
			// 1
			AppConfig.mEffectedBitmap = AppConfig.img_change_to_gray_bitmap;
			ivImagea.setImageBitmap(AppConfig.img_change_to_gray_bitmap);
			bitmap = AppConfig.img_change_to_gray_bitmap;
			break;
		case R.id.img_change_to_relief:
			// 2
			ivImagea.setImageBitmap(AppConfig.img_change_to_relief_bitmap);
			bitmap = AppConfig.img_change_to_relief_bitmap;
			break;
		case R.id.img_change_to_vague:
			// 3
			ivImagea.setImageBitmap(AppConfig.img_change_to_vague_bitmap);
			bitmap = AppConfig.img_change_to_vague_bitmap;
			break;
		case R.id.img_change_to_oil:
			// 4
			ivImagea.setImageBitmap(AppConfig.img_change_to_oil_bitmap);
			bitmap = AppConfig.img_change_to_oil_bitmap;
			break;

		case R.id.img_light:
			// 5
			ivImagea.setImageBitmap(AppConfig.img_light_bitmap);
			bitmap = AppConfig.img_light_bitmap;
			break;
		case R.id.img_change_to_neon:
			// 6
			ivImagea.setImageBitmap(AppConfig.img_change_to_neon_bitmap);
			bitmap = AppConfig.img_change_to_neon_bitmap;
			break;

		case R.id.img_change_to_pixelate:
			// 7
			ivImagea.setImageBitmap(AppConfig.img_change_to_pixelate_bitmap);
			bitmap = AppConfig.img_change_to_pixelate_bitmap;

			break;
		case R.id.img_change_to_invert:
			// 8
			ivImagea.setImageBitmap(AppConfig.img_change_to_invert_bitmap);
			bitmap = AppConfig.img_change_to_invert_bitmap;
			break;
		case R.id.img_change_to_tv:
			// 9
			ivImagea.setImageBitmap(AppConfig.img_change_to_tv_bitmap);
			bitmap = AppConfig.img_change_to_tv_bitmap;
			break;

		case R.id.img_fly:
			// 10
			ivImagea.setImageBitmap(AppConfig.img_fly_bitmap);
			bitmap = AppConfig.img_fly_bitmap;
			break;

		case R.id.img_contrast:
			// 11
			ivImagea.setImageBitmap(AppConfig.img_contrast_bitmap);
			bitmap = AppConfig.img_contrast_bitmap;
			break;

		case R.id.img_change_to_block:
			// 12
			ivImagea.setImageBitmap(AppConfig.img_change_to_block_bitmap);
			bitmap = AppConfig.img_change_to_block_bitmap;
			break;
		case R.id.img_change_to_old:
			// 13
			ivImagea.setImageBitmap(AppConfig.img_change_to_old_bitmap);
			bitmap = AppConfig.img_change_to_old_bitmap;
			break;
		case R.id.img_change_to_sharpen:
			// 14
			ivImagea.setImageBitmap(AppConfig.img_change_to_sharpen_bitmap);
			bitmap = AppConfig.img_change_to_sharpen_bitmap;
			break;
		case R.id.img_change_to_light:
			// 15
			ivImagea.setImageBitmap(AppConfig.img_change_to_light_bitmap);
			bitmap = AppConfig.img_change_to_light_bitmap;
			break;

		case R.id.img_photography:
			// 16
			ivImagea.setImageBitmap(AppConfig.img_photography_bitmap);
			bitmap = AppConfig.img_photography_bitmap;
			break;
		case R.id.img_decrising:
			// 17
			ivImagea.setImageBitmap(AppConfig.img_decrising_bitmap);
			bitmap = AppConfig.img_decrising_bitmap;
			break;

		case R.id.img_rotate:
			// 18
			ivImagea.setImageBitmap(AppConfig.img_rotate_bitmap);
			bitmap = AppConfig.img_rotate_bitmap;
			break;

		case R.id.img_brightness:
			// 19
			ivImagea.setImageBitmap(AppConfig.img_brightness_bitmap);
			bitmap = AppConfig.img_brightness_bitmap;
			break;

		case R.id.img_blur:
			// 20
			ivImagea.setImageBitmap(AppConfig.img_blur_bitmap);
			bitmap = AppConfig.img_blur_bitmap;
			break;

		case R.id.img_roundcorner:
			// 21
			ivImagea.setImageBitmap(AppConfig.img_roundcorner_bitmap);
			bitmap = AppConfig.img_roundcorner_bitmap;
			break;

		case R.id.img_boost:
			// 22
			ivImagea.setImageBitmap(AppConfig.img_boost_bitmap);
			bitmap = AppConfig.img_boost_bitmap;
			break;

		case R.id.iv_effect_save:
			// save();
			System.gc();
			AppConfig.isOrigenal = false;
			AppConfig.isFxEffect = true;
			AppConfig.isStatus = false;
			// BitmapDrawable drawable = (BitmapDrawable)
			// ivImagea.getDrawable();
			// Bitmap bitmap = drawable.getBitmap();
			// AppConfig.bitmapEffect = bitmap;
			// intent = new Intent(context, ActivityOptionsIntents.class);
			// startActivity(intent);
			// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

			// Bitmap bitmap;
			// View v1 = findViewById(R.id.ivImagea);// get ur root view id
			// v1.setDrawingCacheEnabled(true);
			// bitmap = Bitmap.createBitmap(v1.getDrawingCache());
			// v1.setDrawingCacheEnabled(false);

			AppConfig.mEffectedBitmap = bitmap;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			AppConfig.mEffectedBitmap.compress(Bitmap.CompressFormat.JPEG, 40,
					bytes);
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().getAbsolutePath();

			File direct = new File(Environment.getExternalStorageDirectory()
					+ "/" + getResources().getString(R.string.app_name) + "/"
					+ AppConfig.imageName);// img_name + ".jpg");

			if (direct.exists())
				direct.delete();

			File folder = new File(extStorageDirectory, "/"
					+ getResources().getString(R.string.app_name) + "/");
			if (!folder.isDirectory())
				folder.mkdir();

			File f = new File(folder, "CamEffects_"
					+ (folder.listFiles().length + 1) + ".jpg");

			try {
				f.createNewFile();
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				fo.close();
				AppConfig.imageName = null;
				Toast.makeText(ActivityFxEffects.this,
						"Your Image is saved as " + f.getName(),
						Toast.LENGTH_SHORT).show();
				AppConfig.imageName = f.getName();

				Intent intent = new Intent(context,
						ActivityOptionsIntents.class);
				startActivity(intent);
				ActivityFxEffects.this.finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);

			} catch (IOException e) {
				e.printStackTrace();
			}

			// saveBitmapInGallery();
			break;
		case R.id.iv_cross:
			intent = new Intent(context, ActivityOptionsIntents.class);
			startActivity(intent);
			// ActivityFxEffects.this.finish();
			break;

		}
	}

	// private void save() {
	// // TODO Auto-generated method stub
	// BitmapDrawable drawable = (BitmapDrawable) ivImagea.getDrawable();
	// Bitmap bitmap = drawable.getBitmap();
	//
	// File direct = new File(Environment.getExternalStorageDirectory(), "/"
	// + getResources().getString(R.string.app_name) + "/");
	//
	// direct.mkdirs();
	//
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	// currentDateandTime = sdf.format(new Date());
	//
	// File image = new File(direct, currentDateandTime + imgType);
	//
	// boolean success = false;
	//
	// // Encode the file as a PNG image.
	// FileOutputStream outStream;
	// try {
	//
	// outStream = new FileOutputStream(image);
	// bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
	// /* 100 to keep full quality of the image */
	//
	// outStream.flush();
	// outStream.close();
	// success = true;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// if (success) {
	// Toast.makeText(getApplicationContext(), "Image saved with success",
	// Toast.LENGTH_LONG).show();
	// } else {
	// Toast.makeText(getApplicationContext(),
	// "Error during image saving", Toast.LENGTH_LONG).show();
	// }
	// }

	private void initialization() {

		ivImagea = (ImageView) findViewById(R.id.ivImagea);
		img_change_to_gray = (ImageView) findViewById(R.id.img_change_to_gray);
		img_change_to_relief = (ImageView) findViewById(R.id.img_change_to_relief);
		img_change_to_vague = (ImageView) findViewById(R.id.img_change_to_vague);
		img_change_to_oil = (ImageView) findViewById(R.id.img_change_to_oil);
		img_light = (ImageView) findViewById(R.id.img_light);

		img_change_to_neon = (ImageView) findViewById(R.id.img_change_to_neon);
		img_change_to_pixelate = (ImageView) findViewById(R.id.img_change_to_pixelate);
		img_change_to_invert = (ImageView) findViewById(R.id.img_change_to_invert);
		img_change_to_tv = (ImageView) findViewById(R.id.img_change_to_tv);
		img_fly = (ImageView) findViewById(R.id.img_fly);
		img_contrast = (ImageView) findViewById(R.id.img_contrast);

		img_change_to_block = (ImageView) findViewById(R.id.img_change_to_block);
		img_change_to_old = (ImageView) findViewById(R.id.img_change_to_old);
		img_change_to_sharpen = (ImageView) findViewById(R.id.img_change_to_sharpen);
		img_change_to_light = (ImageView) findViewById(R.id.img_change_to_light);
		img_photography = (ImageView) findViewById(R.id.img_photography);

		img_decrising = (ImageView) findViewById(R.id.img_decrising);
		img_rotate = (ImageView) findViewById(R.id.img_rotate);
		img_brightness = (ImageView) findViewById(R.id.img_brightness);

		img_blur = (ImageView) findViewById(R.id.img_blur);
		img_round = (ImageView) findViewById(R.id.img_roundcorner);
		img_boost = (ImageView) findViewById(R.id.img_boost);

		iv_effect_save = (ImageView) findViewById(R.id.iv_effect_save);
		iv_cross = (ImageView) findViewById(R.id.iv_cross);
		ll_effects = (LinearLayout) findViewById(R.id.ll_effects);

		hl_view = (HorizontalScrollView) findViewById(R.id.hl_view);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent mIntent = new Intent(getApplicationContext(),
				ActivityOptionsIntents.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mIntent);
	}
}