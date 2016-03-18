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
		// ͼƬ·��
		Log.i(TAG, fileName);
		String path = getFileStreamPath(fileName).getAbsolutePath();
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		float destWidth = dm.widthPixels;  
		float destHeight= dm.heightPixels ; 
		// ��ȡ����ͼƬ�ߴ�
		BitmapFactory.Options options=new BitmapFactory.Options();
		// ����Ϊtrue��options��Ȼ��Ӧ��ͼƬ��������������Ϊ��ͼƬ�����ڴ�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSampleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) { // ��ͼƬ���������Ļ����ʱ
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
		// �ͷ�bitmapռ�õĿռ�
		BitmapDrawable b = (BitmapDrawable) mPicture.getDrawable();
		b.getBitmap().recycle();
		mPicture.setImageDrawable(null);
	}
}
