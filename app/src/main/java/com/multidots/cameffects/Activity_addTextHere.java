package com.multidots.cameffects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_addTextHere extends Activity implements
		ColorPicker.OnColorChangedListener {
	ImageView ivImagea;
	ImageView iv_SizePlus, iv_ChangeColor, iv_TextAllignment,
			iv_TextRotationForword, iv_TextRotationBackword, iv_check;
	// Button btnSizePlus, btnSizeMinus, btnColorChange;
	TextView txtMyText;
	Context context;
	// Spinner spinnerFontStyle;// , spinnerFontSize;
	public static ArrayList<String> fontStyle = new ArrayList<String>();
	public static ArrayList<String> fontSize = new ArrayList<String>();
	int size = 20, rotation = 0;
	Boolean isSize = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_text_view);
		context = Activity_addTextHere.this;
		initialization();
		fontStyle.clear();

		// TextView fontstyle = new TextView(context);
		// fontstyle.setText("ALGER");
		// Typeface type =
		// Typeface.createFromAsset(context.getAssets(),"ALGER.TTF");
		// fontstyle.setTypeface(type);

		fontStyle.add("ALGER");
		fontStyle.add("BRADHITC");
		fontStyle.add("ITCBLKAD");

		fontSize.add("10");
		fontSize.add("15");
		fontSize.add("20");
		fontSize.add("25");
		fontSize.add("30");
		fontSize.add("35");
		fontSize.add("40");
		fontSize.add("45");
		fontSize.add("50");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int squaresize = dm.widthPixels;

		ivImagea.getLayoutParams().width = squaresize;
		ivImagea.getLayoutParams().height = squaresize;

		ivImagea.setImageBitmap(AppConfig.mEffectedBitmap);

		Bundle bundle = getIntent().getExtras();

		// Extract the data�
		String stuff = bundle.getString(AppConfig.myText);
		txtMyText.setText(stuff);

		// spinnerFontStyle = (Spinner) findViewById(R.id.spinner_category);
		// ArrayAdapter<String> fontStyleTupeFace = new ArrayAdapter<String>(
		// context, android.R.layout.simple_spinner_item, fontStyle);
		// fontStyleTupeFace
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// spinnerFontStyle.setAdapter(fontStyleTupeFace);
		//
		// spinnerFontStyle
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent,
		// View view, int position, long id) {
		// spinnerFontStyle.setSelection(position);
		// String category = spinnerFontStyle.getSelectedItem()
		// .toString() + ".TTF";
		//
		// Typeface type = Typeface.createFromAsset(
		// context.getAssets(), category);
		// txtMyText.setTypeface(type);
		//
		// Toast.makeText(context, category, Toast.LENGTH_LONG)
		// .show();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// }
		// });
		// spinnerFontSize = (Spinner) findViewById(R.id.spinner_fontSize);
		// ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
		// android.R.layout.simple_spinner_item, fontSize);
		// dataAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// spinnerFontSize.setAdapter(dataAdapter);
		//
		// spinnerFontSize.setOnItemSelectedListener(new
		// OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// spinnerFontSize.setSelection(position);
		// String category = spinnerFontSize.getSelectedItem().toString();
		// txtMyText.setTextSize(Integer.parseInt(category));
		//
		// Toast.makeText(context, category, Toast.LENGTH_LONG).show();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// }
		// });

		txtMyText.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

			}
		});
		txtMyText.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				drag(event, v);
				return false;
			}
		});

		iv_SizePlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (isSize) {
				size = size + 5;
				txtMyText.setTextSize(size);
				// if (size == 80) {
				// isSize = false;
				// // iv_SizePlus.setText("-----");
				// }
				// } else {
				// if (size == 20) {
				// isSize = true;
				// // iv_SizePlus.setText("+++++");
				// }
				// size = size - 5;
				// txtMyText.setTextSize(size);
				// }
			}
		});
		// btnSizeMinus.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// size = size - 5;
		// txtMyText.setTextSize(size);
		// }
		// });
		iv_ChangeColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				size = size - 5;
				txtMyText.setTextSize(size);
			}
		});
		iv_TextAllignment.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				getColor(v);
				// if (isSize) {
				// txtMyText.setGravity(Gravity.LEFT);
				// } else {
				// txtMyText.setGravity(Gravity.RIGHT);
				// }

			}
		});
		iv_TextRotationForword.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				rotation = rotation + 10;
				txtMyText.setRotation(rotation);

				System.out.println("Rotation" + rotation);
				if (rotation == 360) {
					rotation = 0;
				}

			}
		});
		iv_TextRotationBackword.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				rotation = rotation - 10;
				txtMyText.setRotation(rotation);

				System.out.println("Rotation" + rotation);
				if (rotation == 360) {
					rotation = 0;
				}

			}
		});
		iv_check.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// Bitmap bitmap;
				// View v1 = findViewById(R.id.imgLayout);// get ur root view id
				// v1.setDrawingCacheEnabled(true);
				// bitmap = Bitmap.createBitmap(v1.getDrawingCache());
				// v1.setDrawingCacheEnabled(false);
				//
				// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				// bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
				// File f = new File(Environment.getExternalStorageDirectory()
				// + File.separator + "test.jpg");
				// try {
				// f.createNewFile();
				// FileOutputStream fo = new FileOutputStream(f);
				// fo.write(bytes.toByteArray());
				// fo.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				//
				AppConfig.isOrigenal = false;
				AppConfig.isFxEffect = true;
				Bitmap bitmap;
				View v1 = findViewById(R.id.imgLayout);// get ur root view id
				v1.setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(v1.getDrawingCache());
				v1.setDrawingCacheEnabled(false);

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
				AppConfig.mEffectedBitmap = bitmap;
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().getAbsolutePath();

				File folder = new File(extStorageDirectory, "/"
						+ getResources().getString(R.string.app_name) + "/");
				if (!folder.isDirectory())
					folder.mkdir();

				File f = new File(folder, "CamEffects_"
						+ (folder.listFiles().length + 1) + ".jpg");

				File file = new File(folder + "/" + AppConfig.imageName);
				if (!file.exists())
					file.delete();
				// {
				// System.out.println("isNotAvailable in device"+AppConfig.imageName);
				// }else {
				// System.out.println("isAvailable in device"+AppConfig.imageName);
				// }
				try {
					f.createNewFile();
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(bytes.toByteArray());
					fo.close();
					AppConfig.imageName = null;
					Toast.makeText(Activity_addTextHere.this,
							"Your Image is saved as " + f.getName(),
							Toast.LENGTH_SHORT).show();
					AppConfig.imageName = f.getName();
					AppConfig.isImageSave = false;
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
	}

	public void drag(MotionEvent event, View v) {

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v
				.getLayoutParams();

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE: {
			params.topMargin = (int) event.getRawY() - (v.getHeight());
			params.leftMargin = (int) event.getRawX() - (v.getWidth() / 2);
			v.setLayoutParams(params);
			break;
		}
		case MotionEvent.ACTION_UP: {
			params.topMargin = (int) event.getRawY() - (v.getHeight());
			params.leftMargin = (int) event.getRawX() - (v.getWidth() / 2);
			v.setLayoutParams(params);
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			v.setLayoutParams(params);
			break;
		}
		}
	}

	private void initialization() {

		txtMyText = (TextView) findViewById(R.id.mytvAddtext);
		ivImagea = (ImageView) findViewById(R.id.iv_Main);

		iv_SizePlus = (ImageView) findViewById(R.id.iv_SizePlus);
		iv_ChangeColor = (ImageView) findViewById(R.id.iv_ChangeColor);
		iv_TextAllignment = (ImageView) findViewById(R.id.iv_TextAllignment);
		iv_TextRotationForword = (ImageView) findViewById(R.id.iv_TextRotationForword);
		iv_TextRotationBackword = (ImageView) findViewById(R.id.iv_TextRotationBackword);
		iv_check = (ImageView) findViewById(R.id.iv_check);

	}

	@Override
	public void colorChanged(String str, int color) {
		Activity_addTextHere.this.txtMyText.setTextColor(color);
	}

	public void getColor(View v) {
		new ColorPicker(context, this, "", Color.BLACK, Color.WHITE).show();
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