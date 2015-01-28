package com.multidots.cameffects;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivityFrameEffects extends BaseActivity {
    Context context;
    HorizontalScrollView hsvFramesBar;
    ImageView ivFrame;
    ImageView imageView, iv_effect_save, iv_cross;
    Resources mResources;
    private int resIdFrame;

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth,
                                     int wantedHeight) {
        if (bitmap.getWidth() == wantedWidth) {
            return bitmap;
        }

        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(),
                (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_effect);
        LoadAdd();
        initialization();
        mResources = getResources();
        // AppConfig.bitmapEffect =
        // Bitmap.createBitmap(AppConfig.mBitmapOriginal);

        imageView.setImageBitmap(AppConfig.mEffectedBitmap);

        context = ActivityFrameEffects.this;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int squaresize = dm.widthPixels;
        //
        // ivFrame.getLayoutParams().width = squaresize - 50;
        // ivFrame.getLayoutParams().height = squaresize - 50;

        // imageView.getLayoutParams().width = squaresize;
        // imageView.getLayoutParams().height = squaresize;

//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//
//		Drawable d = new BitmapDrawable(getResources(),
//				AppConfig.mEffectedBitmap);

        // BitmapFactory.decodeResource(getResources(), d, options);
        // int imageHeight = options.outHeight;
        // int imageWidth = options.outWidth;

//		int imageHeight = d.getIntrinsicHeight();
//		int imageWidth = d.getIntrinsicWidth();

        ivFrame.getLayoutParams().width = squaresize;
        ivFrame.getLayoutParams().height = squaresize;

        hsvFramesBar.setVisibility(View.VISIBLE);
        hsvFramesBar.startAnimation(AnimationUtils.loadAnimation(
                ActivityFrameEffects.this, R.anim.slide_right_to_left));

        imageView.setOnTouchListener(new Touch());

        iv_effect_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppConfig.isStatus = false;
                AppConfig.isOrigenal = false;
                AppConfig.isFxEffect = true;
                Bitmap bitmap;
                View v1 = findViewById(R.id.imgLayout);// get ur root view id
                v1.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                AppConfig.mEffectedBitmap = bitmap;
                v1.setDrawingCacheEnabled(false);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                String extStorageDirectory = Environment
                        .getExternalStorageDirectory().getAbsolutePath();

                File direct = new File(Environment
                        .getExternalStorageDirectory()
                        + "/"
                        + getResources().getString(R.string.app_name)
                        + "/"
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
//					Toast.makeText(ActivityFrameEffects.this,
//							"Your Image is saved as " + f.getName(),
//							Toast.LENGTH_SHORT).show();
                    AppConfig.imageName = f.getName();

                    Intent intent = new Intent(context,
                            ActivityOptionsIntents.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        iv_cross.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ActivityOptionsIntents.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onFramesClick(View v) {

        DeselectAllFrameThumb();

        v.setSelected(true);

        String tag = v.getTag().toString();
        if (tag.equals("cancel")) {
            resIdFrame = 0;
            ivFrame.setImageDrawable(null);
        } else {
            resIdFrame = getResources().getIdentifier("frame_" + tag,
                    "drawable", getPackageName());
            ivFrame.setImageResource(resIdFrame);
            // ivFrame.setImageBitmap(scaleBitmap(BitmapFactory.decodeResource(mResources,
            // resId), GlobalPE.fixedWidth/2, GlobalPE.fixedHeight/2));
        }
    }

    // private void saveBitmapInGallery() {
    // // progressDialog=ProgressDialog.show(RagnarokFilterActivity.this, "",
    // // "Saving Image...");
    // new Thread(new Runnable() {
    // private int chk_error = 0;
    // private File file = null;
    //
    // @SuppressWarnings("deprecation")
    // public void run() {
    //
    // File folder = null;
    //
    // if (Environment.getExternalStorageState().equals(
    // Environment.MEDIA_MOUNTED)) {
    // String extStorageDirectory = Environment
    // .getExternalStorageDirectory().getAbsolutePath();
    //
    // folder = new File(extStorageDirectory, "/"
    // + getResources().getString(R.string.app_name) + "/");
    //
    // if (!folder.isDirectory()) {
    // folder.mkdir();
    // }
    // } else {
    // folder = getDir(
    // getResources().getString(R.string.app_name),
    // MODE_WORLD_READABLE);
    // }
    //
    // file = new File(folder, "Photo_Edit_"
    // + (folder.listFiles().length + 1) + ".PNG");
    //
    // try {
    //
    // FileOutputStream outStream = new FileOutputStream(file);
    // if (resIdFrame != 0) {
    // Bitmap bitmapFrame = scaleBitmap(BitmapFactory
    // .decodeResource(mResources, resIdFrame),
    // AppConfig.fixedWidth, AppConfig.fixedHeight);
    //
    // Bitmap bitmapForSave = Bitmap.createBitmap(
    // AppConfig.fixedWidth, AppConfig.fixedHeight,
    // Config.ARGB_8888);
    // Canvas canvas = new Canvas(bitmapForSave);
    // canvas.drawBitmap(AppConfig.bitmapEffect, new Matrix(),
    // new Paint());
    // canvas.drawBitmap(bitmapFrame, new Matrix(),
    // new Paint());
    //
    // bitmapForSave.compress(Bitmap.CompressFormat.PNG, 100,
    // outStream);
    // if (bitmapFrame != null) {
    // bitmapFrame.recycle();
    // bitmapFrame = null;
    // }
    //
    // } else {
    // AppConfig.bitmapEffect.compress(
    // Bitmap.CompressFormat.PNG, 100, outStream);
    // }
    //
    // outStream.flush();
    // outStream.close();
    // if (outStream != null) {
    // outStream = null;
    // }
    // System.gc();
    //
    // new SingleMediaScanner(ActivityFrameEffects.this, file);
    // chk_error = 0;
    //
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // chk_error = 1;
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // chk_error = 2;
    // } catch (Exception e) {
    // e.printStackTrace();
    // chk_error = 3;
    // }
    //
    // runOnUiThread(new Runnable() {
    //
    // public void run() {
    // try {
    // // progressDialog.dismiss();
    //
    // } catch (Exception e) {
    // }
    // if (chk_error > 0) {
    // Log.e("log_tag",
    // "Error at Save Bitmap in Gallery..."
    // + chk_error);
    // Toast.makeText(ActivityFrameEffects.this,
    // "Error at Save Bitmap in Gallery..."
    // + chk_error,
    // Toast.LENGTH_SHORT).show();
    // } else {
    // Toast.makeText(ActivityFrameEffects.this,
    // "Your Image is saved as " + file.getName(),
    // Toast.LENGTH_SHORT).show();
    //
    // Intent intent = new Intent(context,
    // ActivityOptionsIntents.class);
    // startActivity(intent);
    // overridePendingTransition(android.R.anim.fade_in,
    // android.R.anim.fade_out);
    //
    // }
    //
    // }
    // });
    // }
    // }).start();
    // }

    private void DeselectAllFrameThumb() {
        LinearLayout view = (LinearLayout) hsvFramesBar.getChildAt(0);
        for (int i = 0; i < view.getChildCount(); i++) {
            (view.getChildAt(i)).setSelected(false);
        }
    }

    private void initialization() {
        System.gc();
        imageView = (ImageView) findViewById(R.id.iv_Main);
        ivFrame = (ImageView) findViewById(R.id.ivFrame);
        hsvFramesBar = (HorizontalScrollView) findViewById(R.id.hsvFramesBar);
        iv_effect_save = (ImageView) findViewById(R.id.iv_effect_save);
        iv_cross = (ImageView) findViewById(R.id.iv_cross);
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

    public class SingleMediaScanner implements MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }

    }
}