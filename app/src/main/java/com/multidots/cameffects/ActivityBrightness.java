package com.multidots.cameffects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.multidots.cameffects.effects.ImageProcessing;

public class ActivityBrightness extends BaseActivity {
    Bitmap sourceBitmap;
    ImageView ivImageBrightness, iv_cancle, iv_savebrighess, ivResetBrightBtn;
    Context context;
    private SeekBar seekBarBrightness, seekBarContrast, seekBarSaturation;
    private float brightness = 0;
    private float contrast = 0;
    private float saturation = 0;
    private OnSeekBarChangeListener onBCSChangeListener = new OnSeekBarChangeListener() {

        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

            switch (seekBar.getId()) {
                case R.id.seekBarBrightness:
                    if (fromUser) {
                        if (progress == 100) {
                            brightness = 0;
                        } else {
                            brightness = (progress - 100);
                        }

                    }
                    break;

                case R.id.seekBarContrast:

                    if (fromUser) {
                        if (progress == 100) {
                            contrast = 0;
                        } else {
                            contrast = (progress - 100);
                        }
                        contrast = contrast / 100.f;

                    }

                    break;

                case R.id.seekBarSaturation:
                    if (fromUser) {
                        if (progress == 7) {
                            saturation = 1;
                        } else if (progress < 7) {
                            saturation = (progress - 7);
                        } else if (progress > 7) {
                            saturation = (progress - 7) + 2;
                        }

                    }
                    break;
            }
            Log.e("log_tag", brightness + " : " + contrast + " : " + saturation);
            AppConfig.bitmapTemp = ImageProcessing.applyBCS(
                    AppConfig.mEffectedBitmap, brightness, contrast, saturation);
            ivImageBrightness.setImageBitmap(AppConfig.bitmapTemp);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);
        LoadAdd();
        initialization();


        context = ActivityBrightness.this;
        // sourceBitmap = AppConfig.mBitmapOriginal;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int squaresize = dm.widthPixels;

        ivImageBrightness.getLayoutParams().width = squaresize;
        ivImageBrightness.getLayoutParams().height = squaresize;

        ivImageBrightness.setImageBitmap(AppConfig.mEffectedBitmap);

        iv_savebrighess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				Bitmap bitmap;
//				View v1 = findViewById(R.id.ivImageBrightness);// get ur root view id
//				v1.setDrawingCacheEnabled(true);
//				bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//				v1.setDrawingCacheEnabled(false);
//
//				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//				String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//				File direct = new File(Environment.getExternalStorageDirectory() + "/"
//						+ getResources().getString(R.string.app_name) + "/"
//						+ AppConfig.imageName);// img_name + ".jpg");
//
//				if(direct.exists())
//					direct.delete();
//
//				File folder = new File(extStorageDirectory, "/"+ getResources().getString(R.string.app_name) + "/");
//				if (!folder.isDirectory())
//					folder.mkdir();
//
//				File f = new File(folder, "CamEffects_"+ (folder.listFiles().length + 1) + ".jpg");
//
//				try {
//					f.createNewFile();
//					FileOutputStream fo = new FileOutputStream(f);
//					fo.write(bytes.toByteArray());
//					fo.close();
//					AppConfig.imageName = null;
//					Toast.makeText(ActivityBrightness.this,
//							"Your Image is saved as " + f.getName(),
//							Toast.LENGTH_SHORT).show();
//					AppConfig.imageName = f.getName();
//
//					Intent intent = new Intent(context,
//							ActivityOptionsIntents.class);
//					startActivity(intent);
//					overridePendingTransition(android.R.anim.fade_in,
//							android.R.anim.fade_out);
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

                AppConfig.isOrigenal = false;
                AppConfig.isFxEffect = true;
                AppConfig.mEffectedBitmap = AppConfig.bitmapTemp;
                Intent intent = new Intent(context,
                        ActivityOptionsIntents.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });

        iv_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ActivityOptionsIntents.class);
                startActivity(intent);
                finish();
            }
        });
        ivResetBrightBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                resetBrightnessAll();
            }
        });

    }

    private void resetBrightnessAll() {
        brightness = 0;
        contrast = 0;
        saturation = 0;

        seekBarBrightness.setProgress(100);
        seekBarContrast.setProgress(100);
        seekBarSaturation.setProgress(7);
    }

    private void initialization() {

        System.gc();
        ivImageBrightness = (ImageView) findViewById(R.id.ivImageBrightness);
        iv_cancle = (ImageView) findViewById(R.id.iv_cancle);
        iv_savebrighess = (ImageView) findViewById(R.id.iv_savebrighess);
        ivResetBrightBtn = (ImageView) findViewById(R.id.ivResetBrightBtn);

        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBrightness);
        seekBarContrast = (SeekBar) findViewById(R.id.seekBarContrast);
        seekBarSaturation = (SeekBar) findViewById(R.id.seekBarSaturation);

        seekBarBrightness.setProgress(100);
        seekBarContrast.setProgress(100);
        seekBarSaturation.setProgress(7);

        seekBarBrightness.setOnSeekBarChangeListener(onBCSChangeListener);
        seekBarContrast.setOnSeekBarChangeListener(onBCSChangeListener);
        seekBarSaturation.setOnSeekBarChangeListener(onBCSChangeListener);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent mIntent = new Intent(getApplicationContext(), ActivityOptionsIntents.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }
}