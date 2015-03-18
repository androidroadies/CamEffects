package com.multidots.cameffects;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ActivityHome extends BaseActivity {
    private static final int ACTION_REQUEST_CAMERA = 1004,
			RESULT_LOAD_IMAGE = 1;
    private static final int ID_UP = 1;
    private static final int ID_DOWN = 2;
    private static final int ID_SEARCH = 3;
    private static final int ID_INFO = 4;
    private static final int ID_ERASE = 5;
    private static final int ID_OK = 6;
    Context context;
    ImageView icCamera, icGallary/* , ivMore */;
    boolean cameraUpdate = false;
    Cursor cursor = null;
    int dWidth = 10, dHeight = 10;
    private File outputFilePath;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = ActivityHome.this;
        //LoadAdd();
        //loadInterstitial(null);

		init();
		Display mDisplay = getWindowManager().getDefaultDisplay();
		dWidth = mDisplay.getWidth();
		dHeight = mDisplay.getHeight();
//		if (AppConfig.BitmapStatus) {
//			AppConfig.mEffectedBitmap.recycle();
//		}
        AppConfig.mEffectedBitmap = null;
        AppConfig.mOrigenalBitmap =null;
        AppConfig.isStatus = true;
		icCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cameraUpdate = true;
				Intent intentCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				outputFilePath = new File(Environment
						.getExternalStorageDirectory(), "test.jpg");
				intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(outputFilePath));
				startActivityForResult(intentCamera, ACTION_REQUEST_CAMERA);
			}
		});
		icGallary.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cameraUpdate = false;
				Intent i = new Intent(
						Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		ActionItem nextItem = new ActionItem(ID_DOWN, "Next");// ,
																// getResources().getDrawable(R.drawable.menu_down_arrow));
		ActionItem prevItem = new ActionItem(ID_UP, "Prev");// ,
															// getResources().getDrawable(R.drawable.menu_up_arrow));
		ActionItem searchItem = new ActionItem(ID_SEARCH, "Find");// ,
																	// getResources().getDrawable(R.drawable.menu_search));
		ActionItem infoItem = new ActionItem(ID_INFO, "Info");// ,
																// getResources().getDrawable(R.drawable.menu_info));
		ActionItem eraseItem = new ActionItem(ID_ERASE, "Clear");// ,
																	// getResources().getDrawable(R.drawable.menu_eraser));
		ActionItem okItem = new ActionItem(ID_OK, "OK");// ,
														// getResources().getDrawable(R.drawable.menu_ok));
		prevItem.setSticky(true);
		nextItem.setSticky(true);

		// create QuickAction. Use QuickAction.VERTICAL or
		// QuickAction.HORIZONTAL param to define layout
		// orientation
		final QuickAction quickAction = new QuickAction(this,
				QuickAction.VERTICAL);

		// add action items into QuickAction
		quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(searchItem);
		quickAction.addActionItem(infoItem);
		quickAction.addActionItem(eraseItem);
		quickAction.addActionItem(okItem);

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						// here we can filter which action item was clicked with
						// pos or actionId parameter
						if (actionId == ID_SEARCH) {
							Toast.makeText(getApplicationContext(),
									"Let's do some search action",
									Toast.LENGTH_SHORT).show();
						} else if (actionId == ID_INFO) {
							Toast.makeText(getApplicationContext(),
									"I have no info this time",
									Toast.LENGTH_SHORT).show();
						} else {
							Intent intent = new Intent(context,
									ActivitySaveShare.class);
							startActivity(intent);
							// finish();
							Toast.makeText(getApplicationContext(),
									actionItem.getTitle() + " selected",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				Toast.makeText(getApplicationContext(), "Dismissed",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.isStatus = true;
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("1Hello" + requestCode + "out if " + resultCode);
		if (requestCode == 1004 || cameraUpdate == true) {
			if (resultCode == RESULT_OK) {
				System.out.println("2Hello" + requestCode + "out if "
						+ resultCode);
				outputFilePath = new File(
						Environment.getExternalStorageDirectory(), "test.jpg");
				if (outputFilePath != null) {
					AppConfig.BitmapStatus = true;
					System.out.println("3Hello" + requestCode + "out if "
							+ resultCode);

                    AppConfig.mOrigenalBitmap =null;
                    AppConfig.mEffectedBitmap =null;

                    AppConfig.mOrigenalBitmap = getBitmapFromUri(outputFilePath
							.getAbsolutePath());
					AppConfig.mEffectedBitmap = getBitmapFromUri(outputFilePath
							.getAbsolutePath());

					AppConfig.imgPath = outputFilePath.getAbsolutePath();

					long length = outputFilePath.length();
					length = length / 1024;

					System.out.println("pathcamera" + AppConfig.imgPath
							+ length);
					Intent intent = new Intent(context,
							ActivityOptionsIntents.class);
					startActivity(intent);
				}
				System.gc();

			} else if (resultCode == RESULT_CANCELED) {

//				Toast.makeText(this, "Picture was not taken",
//						Toast.LENGTH_SHORT).show();
            } else {

//				Toast.makeText(this, "Picture was not taken",
//						Toast.LENGTH_SHORT).show();
            }

		} else {

			System.out.println("4Hello" + requestCode + "lllll" + resultCode);
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
					&& null != data) {
				AppConfig.BitmapStatus = true;
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				AppConfig.imgPath = picturePath;

                AppConfig.mOrigenalBitmap =null;
                AppConfig.mEffectedBitmap =null;


				AppConfig.mOrigenalBitmap = getBitmapFromUri(picturePath);
				AppConfig.mEffectedBitmap = getBitmapFromUri(picturePath);

				Intent intent = new Intent(context,
						ActivityOptionsIntents.class);
				startActivity(intent);

			}
		}
	}

	private Bitmap getBitmapFromUri(String picturePath) {
		int orientation = 0;
		try {
			ExifInterface exif = new ExifInterface(picturePath);
			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Matrix matrix = null;
		if (orientation == 6 || orientation == 8 || orientation == 3) {
			matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
			} else if (orientation == 8) {

				matrix.postRotate(-90);
			} else if (orientation == 3) {
				matrix.postRotate(180);
			}
		}

		// Bitmap sourceBitmap = ShrinkBitmap(picturePath, 100, 100);
		dHeight = dHeight - 100;
		dWidth = dWidth - 100;
		System.out.println("aksdasdhasdh123----" + dHeight + "----" + dWidth);
		Bitmap sourceBitmap = ShrinkBitmap(picturePath, dWidth, dWidth);

		if (matrix != null) {
			System.out.println("aksdasdhasdh" + sourceBitmap.getWidth()
					+ sourceBitmap.getHeight());
			return Bitmap.createBitmap(sourceBitmap, 0, 0,
					sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,
					true);
		}
		return sourceBitmap;
	}

	private Bitmap ShrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;

		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);
		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				System.out.println("Height ratio" + heightRatio);
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				System.out.println("width ratio" + widthRatio);
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}

	private void init() {
		icCamera = (ImageView) findViewById(R.id.icCamera);
		icGallary = (ImageView) findViewById(R.id.icGallary);
		// ivMore = (ImageView) findViewById(R.id.ivMore);
	}

    @Override
    public void onBackPressed() {
        //loadInterstitial(null);
        super.onBackPressed();
    }
}