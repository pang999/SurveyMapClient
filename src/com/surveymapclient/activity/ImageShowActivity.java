package com.surveymapclient.activity;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

public class ImageShowActivity extends Activity {
	
	private static final String TAG = "ShowPicture";

	private ImageView mPicture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_picture);
		mPicture=(ImageView) findViewById(R.id.picture);
		String fileName = getIntent().getStringExtra(
				TestCameraActivity.KEY_FILENAME);
		// 图片路径
		Log.i(TAG, fileName);
		String path = getFileStreamPath(fileName).getAbsolutePath();
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		float destWidth = dm.widthPixels;  
		float destHeight= dm.heightPixels ; 
		// 读取本地图片尺寸
		BitmapFactory.Options options=new BitmapFactory.Options();
		// 设置为true，options依然对应此图片，但解码器不会为此图片分配内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSampleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) { // 当图片长宽大于屏幕长宽时
			if (srcWidth < srcHeight) {
				inSampleSize = Math.round(srcHeight / destHeight);
			} else {
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		Matrix matrix = new Matrix();
	    matrix.setRotate(90);
	    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		BitmapDrawable bDrawable = new BitmapDrawable(getResources(), bitmap);
		mPicture.setImageDrawable(bDrawable);	
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!(mPicture.getDrawable() instanceof BitmapDrawable))
			return;
		// 释放bitmap占用的空间
		BitmapDrawable b = (BitmapDrawable) mPicture.getDrawable();
		b.getBitmap().recycle();
		mPicture.setImageDrawable(null);
	}
}
