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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivitySaveShare extends BaseActivity {
    Context context;
    Bitmap sourceBitmap;
    ImageView ivImagea, iv_share, iv_home, iv_save;
    Resources mResources;
    private File f;

    // private int resIdFrame;

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
        setContentView(R.layout.activity_save_share);
        LoadAdd();

        if (AppConfig.isOrigenal)
            sourceBitmap = AppConfig.mOrigenalBitmap;
        else
            sourceBitmap = AppConfig.mEffectedBitmap;

        context = ActivitySaveShare.this;
        initialization();

        // AppConfig.bitmapEffect =
        // Bitmap.createBitmap(AppConfig.mBitmapOriginal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int squaresize = dm.widthPixels;

        ivImagea.getLayoutParams().width = squaresize;
        ivImagea.getLayoutParams().height = squaresize;

        ivImagea.setImageBitmap(AppConfig.mEffectedBitmap);

        iv_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (f != null) {
                    sendIntent();
                } else {
                    saveAndShareBitmap();
                }
            }
        });

        iv_home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        iv_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveBitmap();
                //
                // saveBitmapInGallery();
            }
        });

    }

    private void saveBitmap() {
        Bitmap bitmap;
        View v1 = findViewById(R.id.iv_Main);// get ur root view id
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
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

        f = new File(folder, "CamEffects_"
                + (folder.listFiles().length + 1) + ".jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            AppConfig.imageName = null;
            Toast.makeText(this,
                    "Your Image is saved as " + f.getName(),
                    Toast.LENGTH_SHORT).show();
            AppConfig.imageName = f.getName();

//            Intent intent = new Intent(context,
//                    ActivityOptionsIntents.class);
//            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAndShareBitmap() {
        Bitmap bitmap;
        View v1 = findViewById(R.id.iv_Main);// get ur root view id
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
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

        f = new File(folder, "CamEffects_"
                + (folder.listFiles().length + 1) + ".jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            AppConfig.imageName = null;
            AppConfig.imageName = f.getName();

            sendIntent();

        } catch (IOException e) {
            e.printStackTrace();
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
    // + (folder.listFiles().length + 1) + ".png");
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
    // new SingleMediaScanner(ActivitySaveShare.this, file);
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
    // } else {
    // Toast.makeText(ActivitySaveShare.this,
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

    private void sendIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        intent.setType("image/png");
        startActivity(intent);
    }

    private void initialization() {

        ivImagea = (ImageView) findViewById(R.id.iv_Main);

        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_save = (ImageView) findViewById(R.id.iv_save);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent mIntent = new Intent(getApplicationContext(),
                ActivityHome.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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