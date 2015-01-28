package com.multidots.cameffects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.kal.cropboxapi.CropImageView;
import com.kal.cropboxapi.HighlightView;
import com.kal.cropboxapi.IImage;
import com.kal.cropboxapi.MonitoredActivity;
import com.kal.cropboxapi.Util;

import java.util.concurrent.CountDownLatch;

public class ActivityCrop extends MonitoredActivity {

    private final Handler mHandler = new Handler();
    Context context;
    Bitmap sourceBitmap = null;
    // TextView tv_loadPhoto, tv_openCamera, tv_save;
    Uri imageUri = null;
    // 1;
    LinearLayout ll_cd_container;// ll_open_camera,ll_done,ll_crop_container,ll_load_container,
    // String imgPath;
    int imageId = 0;
    ImageView iv_done, iv_cross;
    // private static final int ACTION_REQUEST_CAMERA = 1004;
    // private File outputFilePath;
    // Cursor cursor = null;
    // ProgressDialog Dialog;
    // int dWidth = 10, dHeight = 10;
    private boolean mCircleCrop = false, mDoFaceDetection = true,
            mScale = true, mScaleUp = true;
    // boolean cameraUpdate = false;
    // ImageView imageView;
    private CropImageView cropImageView;
    private IImage mImage;
    private LayoutInflater inflater;
    private int mAspectX, mAspectY, mOutputX, mOutputY;// , RESULT_LOAD_IMAGE =

    // ImageView imageView;
    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(cropImageView);

            int width = AppConfig.mOrigenalBitmap.getWidth();
            int height = AppConfig.mOrigenalBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right
                        - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                        faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);

            cropImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(cropImageView);

            int width = AppConfig.mOrigenalBitmap.getWidth();
            int height = AppConfig.mOrigenalBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0);
            cropImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (AppConfig.mOrigenalBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (AppConfig.mOrigenalBitmap.getWidth() > 256) {
                mScale = 256.0F / AppConfig.mOrigenalBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(AppConfig.mOrigenalBitmap,
                    0, 0, AppConfig.mOrigenalBitmap.getWidth() / 4,
                    AppConfig.mOrigenalBitmap.getHeight() / 4, matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = cropImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
                        faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != AppConfig.mOrigenalBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    cropImageView.mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    cropImageView.invalidate();
                    if (cropImageView.mHighlightViews.size() == 1) {
                        cropImageView.mCrop = cropImageView.mHighlightViews
                                .get(0);
                        cropImageView.mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        Toast t = Toast.makeText(ActivityCrop.this,
                                "Multi face crop help", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            });
        }
    };

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // System.out.println("1Hello" + requestCode + "out if " + resultCode);
    // if (requestCode == 1004 || cameraUpdate == true) {
    // if (resultCode == RESULT_OK) {
    // System.out.println("2Hello" + requestCode + "out if "
    // + resultCode);
    // outputFilePath = new File(Environment.getExternalStorageDirectory(),
    // "test.jpg");
    // if (outputFilePath != null) {
    // System.out.println("3Hello" + requestCode + "out if "
    // + resultCode);
    // AppConfig.mBitmapOriginal =
    // getBitmapFromUri(outputFilePath.getAbsolutePath());
    // imgPath = outputFilePath.getAbsolutePath();
    //
    // long length = outputFilePath.length();
    // length = length / 1024;
    //
    // System.out.println("pathcamera" + imgPath + length);
    // ll_done.setVisibility(View.VISIBLE);
    // ll_cd_container.setVisibility(View.VISIBLE);
    // // AppConfig.bitmapBackUp =
    // // getBitmapFromUri(outputFilePath.getAbsolutePath());
    // // compressImage(imgPath);
    // }
    // System.gc();
    //
    // // new LoadImagesFromSDCard().execute("" + imageId);
    //
    // } else if (resultCode == RESULT_CANCELED) {
    //
    // Toast.makeText(this, "Picture was not taken",
    // Toast.LENGTH_SHORT).show();
    // } else {
    //
    // Toast.makeText(this, "Picture was not taken",
    // Toast.LENGTH_SHORT).show();
    // }
    //
    // } else {
    // System.out.println("4Hello" + requestCode + "lllll" + resultCode);
    // if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
    // && null != data) {
    // Uri selectedImage = data.getData();
    // String[] filePathColumn = { MediaStore.Images.Media.DATA };
    //
    // cursor = getContentResolver().query(selectedImage,
    // filePathColumn, null, null, null);
    // cursor.moveToFirst();
    //
    // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    // String picturePath = cursor.getString(columnIndex);
    // cursor.close();
    //
    // imgPath = picturePath;
    // AppConfig.mBitmapOriginal = getBitmapFromUri(picturePath);
    // // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    // // AppConfig.mBitmapOriginal = BitmapFactory.decodeFile(picturePath);
    //
    // ll_done.setVisibility(View.VISIBLE);
    // ll_cd_container.setVisibility(View.VISIBLE);
    //
    // }
    // }
    // }

    // public Bitmap compressImage(String rawbitmapPath) {
    //
    // Bitmap compressedImage = null;
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // int actualHeight = options.outHeight;
    // int actualWidth = options.outWidth;
    //
    // // max Height and width values of the compressed image is taken as
    // // 816x612
    // float maxHeight = 816.0f;
    // float maxWidth = 612.0f;
    // float imgRatio = actualWidth / actualHeight;
    // float maxRatio = maxWidth / maxHeight;
    //
    // // width and height values are set maintaining the aspect ratio of the
    // // image
    // if (actualHeight > maxHeight || actualWidth > maxWidth) {
    // if (imgRatio < maxRatio) {
    // imgRatio = maxHeight / actualHeight;
    // actualWidth = (int) (imgRatio * actualWidth);
    // actualHeight = (int) maxHeight;
    // } else if (imgRatio > maxRatio) {
    // imgRatio = maxWidth / actualWidth;
    // actualHeight = (int) (imgRatio * actualHeight);
    // actualWidth = (int) maxWidth;
    // } else {
    // actualHeight = (int) maxHeight;
    // actualWidth = (int) maxWidth;
    //
    // }
    // }
    //
    // // setting inSampleSize value allows to load a scaled down version of
    // // the original image
    // // options.inSampleSize = calculateInSampleSize(options,
    // // actualWidth,actualHeight);
    //
    // // inJustDecodeBounds set to false to load the actual bitmap
    // options.inJustDecodeBounds = false;
    //
    // // this options allow android to claim the bitmap memory if it runs low
    // // on memory
    // options.inPurgeable = true;
    // options.inInputShareable = true;
    // options.inTempStorage = new byte[16 * 1024];
    //
    // try {
    // // load the bitmap from its path
    // compressedImage = BitmapFactory.decodeFile(rawbitmapPath, options);
    // } catch (OutOfMemoryError exception) {
    // exception.printStackTrace();
    //
    // }
    // return compressedImage;
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop);
        LoadAdd();
        context = ActivityCrop.this;
        inflater = LayoutInflater.from(this);

        init();

        // Dialog = new ProgressDialog(context);
        // Dialog.setMessage("Loading Effect..");

        // if (sourceBitmap != null) {
        // ll_done.setVisibility(View.VISIBLE);
        // ll_cd_container.setVisibility(View.VISIBLE);
        // }
        // AppConfig.bitmapEffect =
        // Bitmap.createBitmap(AppConfig.mOrigenalBitmap);
        if (AppConfig.isStatus == true) {
            System.out.println("AppConfig.----in crop" + AppConfig.isStatus);
            AppConfig.mEffectedBitmap = Bitmap
                    .createBitmap(AppConfig.mOrigenalBitmap);
            sourceBitmap = AppConfig.mEffectedBitmap;
        } else {
            System.out.println("AppConfig.isStatus is:----in crop"
                    + AppConfig.isStatus);
            AppConfig.mEffectedBitmap = Bitmap
                    .createBitmap(AppConfig.mEffectedBitmap);
            sourceBitmap = AppConfig.mEffectedBitmap;
        }

        startFaceDetection();

        // imageView.setImageBitmap(AppConfig.mEffectedBitmap);

        // Display mDisplay = getWindowManager().getDefaultDisplay();
        // dWidth = mDisplay.getWidth();
        // dHeight = mDisplay.getHeight();

        // imageView = (ImageView) findViewById(R.id.image_mainAct);
        // imageView.setImageBitmap(AppConfig.bitmapBackUp);

        // BitmapFactory.Options bfo = new BitmapFactory.Options();
        // bfo.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera,
        // bfo);
        //
        // AppConfig.fixedWidth = (int) (bfo.outWidth * AppConfig.scale);
        // AppConfig.fixedHeight = (int) (bfo.outHeight * AppConfig.scale);
        //
        // Log.i("log_tag", AppConfig.fixedWidth + " : " +
        // AppConfig.fixedHeight);
        // mAspectX = 1;
        // mAspectY = 1;
        // mOutputX = AppConfig.fixedWidth;
        // mOutputY = AppConfig.fixedHeight;
        // Log.e("log_tag", AppConfig.fixedWidth + " : " +
        // AppConfig.fixedHeight);

        iv_cross.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ActivityOptionsIntents.class);
                startActivity(intent);
                // finish();
            }
        });
        iv_done.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                // compressImage(imgPath);
                AppConfig.isOrigenal = false;
                AppConfig.isFxEffect = true;
                onSaveClicked();
                // if (AppConfig.isCrop) {
                // AppConfig.isCrop = false;
                //
                // onSaveClicked();
                // } else {
                // System.out.println("Done"+sourceBitmap.getWidth()+"-----=" +
                // sourceBitmap.getHeight() );
                //
                // AppConfig.bitmapCrop =
                // Bitmap.createScaledBitmap(AppConfig.mOrigenalBitmap,
                // dWidth,dHeight, true);
                // AppConfig.bitmapCrop = AppConfig.mOrigenalBitmap;
                // }
                // new Handler().postDelayed(new Runnable() {
                // @Override
                // public void run() {
                Intent intent = new Intent(context,
                        ActivityOptionsIntents.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();
                // }
                // }, 3000);

            }
        });
        // ll_crop_container.setOnClickListener(new OnClickListener() {
        // @SuppressLint("SimpleDateFormat")
        // public void onClick(View v) {
        // AppConfig.isCrop = true;
        // startFaceDetection();
        // }
        // });
        // ll_open_camera.setOnClickListener(new OnClickListener() {
        //
        // public void onClick(View v) {
        //
        // cameraUpdate = true;
        // Intent intentCamera = new Intent(
        // MediaStore.ACTION_IMAGE_CAPTURE);
        // outputFilePath = new File(Environment
        // .getExternalStorageDirectory(), "test.jpg");
        // intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
        // Uri.fromFile(outputFilePath));
        // startActivityForResult(intentCamera, ACTION_REQUEST_CAMERA);
        //
        // }
        // });

        // ll_load_container.setOnClickListener(new OnClickListener() {
        // @SuppressLint("SimpleDateFormat")
        // public void onClick(View v) {
        // cameraUpdate = false;
        // Intent i = new Intent(
        // Intent.ACTION_PICK,
        // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(i, RESULT_LOAD_IMAGE);
        // }
        // });

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        // if (AppConfig.mBitmap != null) {
        cropImageView = (CropImageView) inflater.inflate(
                R.layout.crop_image_view, null);
        cropImageView.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        ((LinearLayout) findViewById(R.id.llCropBoxContainer))
                .addView(cropImageView);

        if (AppConfig.isStatus) {
            System.out.println("In if crop resume");
            cropImageView.setImageBitmapResetBase(sourceBitmap, true);
        } else {
            System.out.println("In else crop resume");
            cropImageView.setImageBitmapResetBase(AppConfig.mOrigenalBitmap,
                    true);
        }

        // if (cameraUpdate) {
        // long length = outputFilePath.length();
        // length = length / 1024;
        // System.out.println("path" + imgPath + length);
        // }

        // Bitmap b = compressImage(imgPath);
        // startFaceDetection();
        // }
    }

    private void startFaceDetection() {
        if (isFinishing()) {
            return;
        }

        // cropImageView.setImageBitmapResetBase(AppConfig.mBitmap, true);

        Util.startBackgroundJob(this, null, "Please wait\u2026",
                new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        final Bitmap b = (mImage != null) ? mImage
                                .fullSizeBitmap(IImage.UNCONSTRAINED,
                                        1024 * 1024)
                                : AppConfig.mOrigenalBitmap;
                        mHandler.post(new Runnable() {
                            public void run() {
                                if (b != AppConfig.mOrigenalBitmap && b != null) {
                                    cropImageView.setImageBitmapResetBase(b,
                                            true);
                                    AppConfig.mOrigenalBitmap.recycle();
                                    AppConfig.mOrigenalBitmap = b;
                                }
                                if (cropImageView.getScale() == 1F) {
                                    cropImageView.center(true, true);
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        mRunFaceDetection.run();
                    }
                }, mHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cropImageView = null;
        ((LinearLayout) findViewById(R.id.llCropBoxContainer)).removeAllViews();

    }

    // private Bitmap getBitmapFromUri(String picturePath) {
    // int orientation = 0;
    // try {
    // ExifInterface exif = new ExifInterface(picturePath);
    // orientation = exif
    // .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // Matrix matrix = null;
    // if (orientation == 6 || orientation == 8 || orientation == 3) {
    // matrix = new Matrix();
    // if (orientation == 6) {
    //
    // matrix.postRotate(90);
    //
    // } else if (orientation == 8) {
    //
    // matrix.postRotate(-90);
    // } else if (orientation == 3) {
    //
    // matrix.postRotate(180);
    // }
    //
    // }
    //
    // // Bitmap sourceBitmap = ShrinkBitmap(picturePath, 100, 100);
    // Bitmap sourceBitmap = ShrinkBitmap(picturePath, dWidth, dWidth);
    // System.out.println("aksdasdhasdh123----"+dHeight+"----"+dWidth);
    // if (matrix != null) {
    // System.out.println("aksdasdhasdh"+sourceBitmap.getWidth()+sourceBitmap.getHeight());
    // return Bitmap.createBitmap(sourceBitmap, 0, 0,
    // sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,
    // true);
    // }
    // return sourceBitmap;
    // }

    // private Bitmap ShrinkBitmap(String file, int width, int height) {
    //
    // BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
    // bmpFactoryOptions.inJustDecodeBounds = true;
    //
    // Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
    //
    // int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
    // / (float) height);
    // int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
    // / (float) width);
    // if (heightRatio > 1 || widthRatio > 1) {
    // if (heightRatio > widthRatio) {
    // System.out.println("Height ratio"+heightRatio);
    // bmpFactoryOptions.inSampleSize = heightRatio;
    // } else {
    // System.out.println("width ratio"+widthRatio);
    // bmpFactoryOptions.inSampleSize = widthRatio;
    // }
    // }
    //
    // bmpFactoryOptions.inJustDecodeBounds = false;
    // bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
    // return bitmap;
    // }

    // public static String convertImageUriToFile(Uri imageUri, Activity
    // activity) {
    // Cursor cursor = null;
    // int imageID = 0;
    //
    // try {
    // /*********** Which columns values want to get *******/
    // String[] proj = { MediaStore.Images.Media.DATA,
    // MediaStore.Images.Media._ID,
    // MediaStore.Images.Thumbnails._ID,
    // MediaStore.Images.ImageColumns.ORIENTATION };
    // System.out.println("uri:" + imageUri + " proj:" + proj);
    // cursor = activity.managedQuery(imageUri, // Get data for specific
    // // image URI
    // proj, // Which columns to return
    // null, // WHERE clause; which rows to return (all rows)
    // null, // WHERE clause selection arguments (none)
    // null // Order-by clause (ascending by name)
    // );
    //
    // int columnIndex = cursor
    // .getColumnIndexOrThrow(MediaStore.Images.Media._ID);
    // int columnIndexThumb = cursor
    // .getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
    // int file_ColumnIndex = cursor
    // .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    // // int orientation_ColumnIndex =
    // //
    // cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
    //
    // int size = cursor.getCount();
    //
    // /******* If size is 0, there are no images on the SD Card. *****/
    //
    // if (size == 0) {
    // // imageDetails.setText("No Image");
    // } else {
    //
    // int thumbID = 0;
    // if (cursor.moveToFirst()) {
    //
    // /**************** Captured image details ************/
    //
    // /***** Used to show image on view in LoadImagesFromSDCard class ******/
    // imageID = cursor.getInt(columnIndex);
    //
    // thumbID = cursor.getInt(columnIndexThumb);
    //
    // String Path = cursor.getString(file_ColumnIndex);
    //
    // // String orientation =
    // // cursor.getString(orientation_ColumnIndex);
    //
    // // String CapturedImageDetails =
    // // " CapturedImageDetails : \n\n"
    // // + " ImageID :"
    // // + imageID
    // // + "\n"
    // // + " ThumbID :"
    // // + thumbID + "\n" + " Path :" + Path + "\n";
    //
    // // Show Captured Image detail on view
    // // imageDetails.setText(CapturedImageDetails);
    //
    // }
    // }
    // } finally {
    // if (cursor != null) {
    // // cursor.close();
    // }
    // }
    //
    // return "" + imageID;
    // }

    /**
     * Async task for loading the images from the SD card.
     *
     * @author Android Example
     */
    // Class with extends AsyncTask class
    // public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {
    //
    // Bitmap mBitmap;
    //
    // protected void onPreExecute() {
    // /****** NOTE: You can call UI Element here. *****/
    //
    // // UI Element
    // // Dialog.setMessage("Loading image from Sdcard..");
    // // Dialog.show();
    // }
    //
    // // Call after onPreExecute method
    // protected Void doInBackground(String... urls) {
    //
    // Bitmap bitmap = null;
    // Bitmap newBitmap = null;
    // Uri uri = null;
    //
    // try {
    //
    // /**
    // * Uri.withAppendedPath Method Description Parameters baseUri
    // * Uri to append path segment to pathSegment encoded path
    // * segment to append Returns a new Uri based on baseUri with the
    // * given segment appended to the path
    // */
    //
    // uri = Uri.withAppendedPath(
    // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
    // + urls[0]);
    //
    // /************** Decode an input stream into a bitmap. *********/
    // bitmap =
    // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
    //
    // System.out.println("bitmap"+bitmap+"---->>>>"+bitmap.getWidth()+"======"
    // + bitmap.getHeight());
    // if (bitmap != null) {
    //
    // // Display mDisplay =
    // // c.getWindowManager().getDefaultDisplay();
    // // displayWidth = mDisplay.getWidth();
    // /********* Creates a new bitmap, scaled from an existing bitmap.
    // ***********/
    //
    // System.out.println("on Create bitmap"+bitmap.getWidth()+"======" +
    // bitmap.getHeight());
    //
    // newBitmap = Bitmap.createScaledBitmap(bitmap, dWidth/2 ,dHeight/2, true);
    // bitmap.recycle();
    //
    // if (newBitmap != null) {
    //
    // mBitmap = newBitmap;
    // }
    // }
    // } catch (IOException e) {
    // // Error fetching image, try to recover
    //
    // /********* Cancel execution of this task. **********/
    // cancel(true);
    // }
    //
    // return null;
    // }
    //
    // protected void onPostExecute(Void unused) {
    //
    // // NOTE: You can call UI Element here.
    //
    // // Close progress dialog
    // // Dialog.dismiss();
    //
    // if (mBitmap != null) {
    // // imageView.setImageBitmap(mBitmap);
    // AppConfig.mOrigenalBitmap = mBitmap;
    // // imageView.setImageBitmap(AppConfig.original_bitmap);
    // // Intent intent = new Intent(context,
    // // RagnarokFilterActivity.class);
    // // startActivity(intent);
    //
    // // img_SavePicture.setVisibility(View.VISIBLE);
    // // btnLoadPicture.setVisibility(View.GONE);
    // // btnOpenCamera.setVisibility(View.GONE);
    // }
    // // sourceBitmap =
    // // BitmapFactory.decodeResource(imageView.setImageBitmap(mBitmap));
    //
    // }
    //
    // Runnable mRunFaceDetection = new Runnable() {
    // float mScale = 1F;
    // Matrix mImageMatrix;
    // FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
    // int mNumFaces;
    //
    // // For each face, we create a HightlightView for it.
    // private void handleFace(FaceDetector.Face f) {
    // PointF midPoint = new PointF();
    //
    // int r = ((int) (f.eyesDistance() * mScale)) * 2;
    // f.getMidPoint(midPoint);
    // midPoint.x *= mScale;
    // midPoint.y *= mScale;
    //
    // int midX = (int) midPoint.x;
    // int midY = (int) midPoint.y;
    //
    // HighlightView hv = new HighlightView(cropImageView);
    //
    // int width = AppConfig.mBitmapOriginal.getWidth();
    // int height = AppConfig.mBitmapOriginal.getHeight();
    //
    // Rect imageRect = new Rect(0, 0, width, height);
    //
    // RectF faceRect = new RectF(midX, midY, midX, midY);
    // faceRect.inset(-r, -r);
    // if (faceRect.left < 0) {
    // faceRect.inset(-faceRect.left, -faceRect.left);
    // }
    //
    // if (faceRect.top < 0) {
    // faceRect.inset(-faceRect.top, -faceRect.top);
    // }
    //
    // if (faceRect.right > imageRect.right) {
    // faceRect.inset(faceRect.right - imageRect.right,
    // faceRect.right - imageRect.right);
    // }
    //
    // if (faceRect.bottom > imageRect.bottom) {
    // faceRect.inset(faceRect.bottom - imageRect.bottom,
    // faceRect.bottom - imageRect.bottom);
    // }
    //
    // hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
    // mAspectX != 0 && mAspectY != 0);
    //
    // cropImageView.add(hv);
    // }
    //
    // // Create a default HightlightView if we found no face in the
    // // picture.
    // private void makeDefault() {
    // HighlightView hv = new HighlightView(cropImageView);
    //
    // int width = AppConfig.mBitmapOriginal.getWidth();
    // int height = AppConfig.mBitmapOriginal.getHeight();
    //
    // Rect imageRect = new Rect(0, 0, width, height);
    //
    // // make the default size about 4/5 of the width or height
    // int cropWidth = Math.min(width, height) * 4 / 5;
    // int cropHeight = cropWidth;
    //
    // if (mAspectX != 0 && mAspectY != 0) {
    // if (mAspectX > mAspectY) {
    // cropHeight = cropWidth * mAspectY / mAspectX;
    // } else {
    // cropWidth = cropHeight * mAspectX / mAspectY;
    // }
    // }
    //
    // int x = (width - cropWidth) / 2;
    // int y = (height - cropHeight) / 2;
    //
    // RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
    // hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
    // mAspectX != 0 && mAspectY != 0);
    // cropImageView.add(hv);
    // }
    //
    // // Scale the image down for faster face detection.
    // private Bitmap prepareBitmap() {
    // if (AppConfig.mBitmapOriginal == null) {
    // return null;
    // }
    //
    // // 256 pixels wide is enough.
    // if (AppConfig.mBitmapOriginal.getWidth() > 256) {
    // mScale = 256.0F / AppConfig.mBitmapOriginal.getWidth();
    // }
    // Matrix matrix = new Matrix();
    // matrix.setScale(mScale, mScale);
    // Bitmap faceBitmap = Bitmap.createBitmap(
    // AppConfig.mBitmapOriginal, 0, 0,
    // AppConfig.mBitmapOriginal.getWidth(),
    // AppConfig.mBitmapOriginal.getHeight(), matrix, true);
    // return faceBitmap;
    // }
    //
    // public void run() {
    // mImageMatrix = cropImageView.getImageMatrix();
    // Bitmap faceBitmap = prepareBitmap();
    //
    // mScale = 1.0F / mScale;
    // if (faceBitmap != null && mDoFaceDetection) {
    // FaceDetector detector = new FaceDetector(
    // faceBitmap.getWidth(), faceBitmap.getHeight(),
    // mFaces.length);
    // mNumFaces = detector.findFaces(faceBitmap, mFaces);
    // }
    //
    // if (faceBitmap != null
    // && faceBitmap != AppConfig.mBitmapOriginal) {
    // faceBitmap.recycle();
    // }
    //
    // mHandler.post(new Runnable() {
    // public void run() {
    // cropImageView.mWaitingToPick = mNumFaces > 1;
    // if (mNumFaces > 0) {
    // for (int i = 0; i < mNumFaces; i++) {
    // handleFace(mFaces[i]);
    // }
    // } else {
    // makeDefault();
    // }
    // cropImageView.invalidate();
    // if (cropImageView.mHighlightViews.size() == 1) {
    // cropImageView.mCrop = cropImageView.mHighlightViews
    // .get(0);
    // cropImageView.mCrop.setFocus(true);
    // }
    //
    // if (mNumFaces > 1) {
    // Toast t = Toast.makeText(Mainactivity.this,
    // "Multi face crop help", Toast.LENGTH_SHORT);
    // t.show();
    // }
    // }
    // });
    // }
    // };
    //
    // }
    private void onSaveClicked() {
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (cropImageView.mSaving)
            return;

        if (cropImageView.mCrop == null) {
            return;
        }

        cropImageView.mSaving = true;

        Rect r = cropImageView.mCrop.getCropRect();

        int width = r.width();
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        Bitmap croppedImage = Bitmap.createBitmap(width, height,
                mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(AppConfig.mOrigenalBitmap, r, dstRect, null);
        }

        if (mCircleCrop) {
            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle. So we fill in the
            // area around the circle with alpha. Note the all important
            // PortDuff.Mode.CLEAR.
            Canvas c = new Canvas(croppedImage);
            Path p = new Path();
            p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
            c.clipPath(p, Region.Op.DIFFERENCE);
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        }

		/* If the output is required to a specific size then scale or fill */
        if (mOutputX != 0 && mOutputY != 0) {
            if (mScale) {
                /* Scale the image to the required dimensions */
                Bitmap old = croppedImage;
                croppedImage = Util.transform(new Matrix(), croppedImage,
                        mOutputX, mOutputY, mScaleUp);
                if (old != croppedImage) {
                    old.recycle();
                }
            } else {

				/*
				 * Don't scale the image crop it to the size requested. Create
				 * an new image with the cropped image in the center and the
				 * extra space filled.
				 */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
                        Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(b);

                Rect srcRect = cropImageView.mCrop.getCropRect();
                Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                int dx = (srcRect.width() - dstRect.width()) / 2;
                int dy = (srcRect.height() - dstRect.height()) / 2;

				/* If the srcRect is too big, use the center part of it. */
                srcRect.inset(Math.max(0, dx), Math.max(0, dy));

				/* If the dstRect is too big, use the center part of it. */
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

				/* Draw the cropped bitmap in the center */
                canvas.drawBitmap(AppConfig.mOrigenalBitmap, srcRect, dstRect,
                        null);

				/* Set the cropped bitmap as the new bitmap */
                croppedImage.recycle();
                croppedImage = b;

            }

        }
        // DisplayMetrics metrics = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //
        // croppedImage.setDensity(metrics.densityDpi);
        // Log.e("log_tag", croppedImage.getWidth() + " : " +
        // metrics.densityDpi);
        AppConfig.mOrigenalBitmap = null;

        // AppConfig.mOrigenalBitmap = Bitmap.createBitmap(croppedImage);
        AppConfig.mEffectedBitmap = Bitmap.createBitmap(croppedImage);
        AppConfig.mOrigenalBitmap = AppConfig.mEffectedBitmap;
    }

    private void init() {
        System.gc();
        // imageView = (ImageView) findViewById(R.id.image);
        // ll_done = (LinearLayout) findViewById(R.id.ll_done);
        // ll_open_camera = (LinearLayout) findViewById(R.id.ll_open_camera);
        // ll_load_container = (LinearLayout)
        // findViewById(R.id.ll_load_container);
        // ll_crop_container = (LinearLayout)
        // findViewById(R.id.ll_crop_container);
        // ll_cd_container = (LinearLayout) findViewById(R.id.ll_cd_container);

        // tv_loadPhoto = (TextView) findViewById(R.id.tv_loadPhoto);
        // tv_openCamera = (TextView) findViewById(R.id.tv_openCamera);
        // tv_save = (TextView) findViewById(R.id.tv_save);

        iv_done = (ImageView) findViewById(R.id.iv_done);
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
}