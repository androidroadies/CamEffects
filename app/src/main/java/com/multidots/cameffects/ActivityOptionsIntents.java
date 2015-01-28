package com.multidots.cameffects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityOptionsIntents extends BaseActivity {
    Context context;
    ImageView iv_back, iv_next, iv_crop, iv_effects, iv_color, iv_frames,
            iv_text;
    // Bitmap sourceBitmap;
    ActivityOptionsIntents CameraActivity = null;
    ImageView imageView;
    EditText etTextHere;

    // Bitmap myBitmap;
    // Boolean isStatus = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_effect);
        init();
        LoadAdd();
        CameraActivity = this;
        context = ActivityOptionsIntents.this;
        // sourceBitmap = AppConfig.mBitmapOriginal;
        LayoutInflater.from(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int squaresize = dm.widthPixels;

        imageView.getLayoutParams().width = squaresize;
        imageView.getLayoutParams().height = squaresize;

        if (AppConfig.isOrigenal) {
            System.out.println("ActivityOptionsIntents :" + AppConfig.isOrigenal);
            AppConfig.mEffectedBitmap = AppConfig.mOrigenalBitmap;
        } else {
            System.out.println("ActivityOptionsIntents :" + AppConfig.isOrigenal);
            AppConfig.mEffectedBitmap = AppConfig.mEffectedBitmap;
        }

        // System.out.println("" + AppConfig.imageName);
        // if (AppConfig.imageName != null) {
        // File direct = new File(Environment.getExternalStorageDirectory() +
        // "/"
        // + getResources().getString(R.string.app_name) + "/"
        // + AppConfig.imageName);// img_name + ".jpg");

        // if(!direct.exists()){
        // System.out.println("Ahiya available nathi image ahiya su gote chho bhai");
        // }else{
        // isStatus = false;
        // }
        // myBitmap = BitmapFactory.decodeFile(direct.getAbsolutePath());
        // myBitmap = AppConfig.bitmapEffect;

        // }
        // if (isStatus == true) {
        // AppConfig.bitmapEffect =
        // Bitmap.createBitmap(AppConfig.mBitmapOriginal);
        // System.out.println("Han bhai han sachu j chhe--------"+
        // AppConfig.isStatus);
        // // imageView.setImageBitmap(AppConfig.mBitmapOriginal);
        // System.gc();
        // } else {
        // AppConfig.bitmapEffect = myBitmap;
        // System.out.println("AppConfig.isStatus is:----"+ AppConfig.isStatus);
        // // imageView.setImageBitmap(AppConfig.bitmapEffect);
        // System.gc();
        // }

        // imageView.setImageBitmap(AppConfig.bitmapEffect);

        iv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(context, ActivityHome.class);
//				startActivity(intent);
                finish();
            }
        });
        iv_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitySaveShare.class);
                loadInterstitial(intent);
//                startActivity(intent);
                // finish();
            }
        });
        iv_crop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.isStatus == false) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder
                            .setTitle("If you click yes your all changes will be undo.....are you sure you want to undo all changes...????");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click yes to Move...!!!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            AppConfig.mEffectedBitmap = AppConfig.mOrigenalBitmap;
                                            ActivityOptionsIntents.this
                                                    .finish();
                                            Intent intent = new Intent(context,
                                                    ActivityCrop.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // if this button is clicked, just
                                            // close
                                            // the dialog box and do nothing
                                            dialog.cancel();
                                            // Intent intent = new Intent(
                                            // context,
                                            // ActivityOptionsIntents.class);
                                            // startActivity(intent);
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    Intent intent = new Intent(context, ActivityCrop.class);
                    startActivity(intent);
                }
            }
        });
        iv_effects.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityFxEffects.class);
                startActivity(intent);
                // finish();
            }
        });
        iv_color.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityBrightness.class);
                startActivity(intent);
                ActivityOptionsIntents.this.finish();
            }
        });
        iv_frames.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityFrameEffects.class);
                startActivity(intent);
                // finish();
            }
        });
        iv_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addTextHere();
                // Intent intent = new Intent(context,
                // ActivityTextEffects.class);
                // startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if (AppConfig.mBitmap != null) {
        // cropImageView = (CropImageView) inflater.inflate(
        // R.layout.crop_image_view, null);
        // cropImageView.setLayoutParams(new LinearLayout.LayoutParams(
        // LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        // ((LinearLayout) findViewById(R.id.llCropBoxContainer))
        // .addView(cropImageView);
        // cropImageView.setImageBitmapResetBase(AppConfig.mBitmapOriginal,
        // true);

        // if(AppConfig.isImageSave){
        // System.out.println("OnResume true");
        // imageView.setImageBitmap(AppConfig.bitmapEffect);
        // }else{
        // System.out.println("OnResume false");
        // AppConfig.bitmapEffect = myBitmap;
        imageView.setImageBitmap(AppConfig.mEffectedBitmap);
        // }
    }

    private void init() {
        System.gc();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_next = (ImageView) findViewById(R.id.iv_next);

        iv_crop = (ImageView) findViewById(R.id.iv_crop);
        iv_effects = (ImageView) findViewById(R.id.iv_effects);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        iv_frames = (ImageView) findViewById(R.id.iv_frames);
        iv_text = (ImageView) findViewById(R.id.iv_text);

        imageView = (ImageView) findViewById(R.id.iv_Main);

    }

    private void addTextHere() {
        System.gc();
        LayoutInflater layoutInflater = LayoutInflater.from(ActivityOptionsIntents.this);
        View addTextView = layoutInflater.inflate(R.layout.add_text_here_dialog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityOptionsIntents.this);
        alertDialog.setView(addTextView);
        etTextHere = (EditText) addTextView.findViewById(R.id.input_top_up_value);
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!etTextHere.getText().toString().isEmpty()) {
                            Intent intent = new Intent(context,
                                    Activity_addTextHere.class);

                            Bundle bundle = new Bundle();
                            bundle.putString(AppConfig.myText, etTextHere
                                    .getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);

                            ActivityOptionsIntents.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please Enter Text...", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        alertDialog.setNeutralButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
        return;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent mIntent = new Intent(getApplicationContext(), ActivityHome.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
    }
}