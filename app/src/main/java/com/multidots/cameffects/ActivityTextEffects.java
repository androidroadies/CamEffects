package com.multidots.cameffects;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityTextEffects extends Activity implements OnTouchListener {
	ImageView imageView;
	TextView dragTextView;

	final static float STEP = 200;
	TextView mytv;
	float mRatio = 1.0f;
	int mBaseDist;
	float mBaseRatio;
	float fontsize = 13;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_drop);
		init();

//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		int squaresize = dm.widthPixels;
//
//		imageView.getLayoutParams().width = squaresize;
//		imageView.getLayoutParams().height = squaresize;
//
//		imageView.setImageBitmap(AppConfig.bitmapEffect);

//		imageView.setOnTouchListener(new Touch());
		mytv = (TextView) findViewById(R.id.mytv);
		mytv.setTextSize(mRatio + 20);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			int action = event.getAction();
			int pureaction = action & MotionEvent.ACTION_MASK;
			if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
				mBaseDist = getDistance(event);
				mBaseRatio = mRatio;
			} else {
				float delta = (getDistance(event) - mBaseDist) / STEP;
				float multi = (float) Math.pow(2, delta);
				mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
				mytv.setTextSize(mRatio + 20);
			}
		}
		return true;
	}

	int getDistance(MotionEvent event) {
		int dx = (int) (event.getX(0) - event.getX(1));
		int dy = (int) (event.getY(0) - event.getY(1));
		return (int) (Math.sqrt(dx * dx + dy * dy));
	}

	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	private void init() {
		imageView = (ImageView) findViewById(R.id.iv_Main);
//		 dragTextView = (TextView) findViewById(R.id.test);
	}
}