package com.surveymapclient.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class TestCameraActivity extends Activity implements OnClickListener,SurfaceHolder.Callback{

	private static final String TAG = "TestCameraActivity";
	public static final String KEY_FILENAME = "filename";
	private Button mTakePhoto;
	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private String mFileName;
	private OrientationEventListener mOrEventListener; // 设备方向监听器
	private Boolean mCurrentOrientation; // 当前设备方向 横屏false,竖屏true
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testcamera);
		mTakePhoto = (Button) findViewById(R.id.take_photo);
		mTakePhoto.setOnClickListener(this); // 拍照按钮监听器

		startOrientationChangeListener(); // 启动设备方向监听器
		mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this); // 回调接口
	};
	
	/* 图像数据处理还未完成时的回调函数 */
	private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			// 一般显示进度条
		}
	};
	/* 图像数据处理完成后的回调函数 */
	
	private Camera.PictureCallback mJpeg=new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 保存图片
			mFileName = UUID.randomUUID().toString() + ".jpg";
			Log.i(TAG, mFileName);
			FileOutputStream out = null;
			try {
				out = openFileOutput(mFileName, Context.MODE_PRIVATE);
				byte[] newData = null;
				if (mCurrentOrientation) {
					// 竖屏时，旋转图片再保存
					Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					Matrix matrix = new Matrix();
					matrix.setRotate(0);
					Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
							oldBitmap.getWidth(), oldBitmap.getHeight(),
							matrix, true);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
					newData = baos.toByteArray();
					out.write(newData);
				} else {
					out.write(data);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Intent i = new Intent(TestCameraActivity.this, CameraActivity.class);
			Bundle bundle=new Bundle();
            bundle.putString(TestCameraActivity.KEY_FILENAME, mFileName);
//			intent.putExtra(TestCameraActivity.KEY_FILENAME, selectedImagePath);
            bundle.putInt("Where", 0);
            i.putExtras(bundle);
			startActivity(i);
			finish();
		}
	};

	private final void startOrientationChangeListener() {
		mOrEventListener = new OrientationEventListener(this) {
			@Override
			public void onOrientationChanged(int rotation) {
				if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
						|| ((rotation >= 135) && (rotation <= 225))) {// portrait
					mCurrentOrientation = true;
					Log.i(TAG, "竖屏");
				} else if (((rotation > 45) && (rotation < 135))
						|| ((rotation > 225) && (rotation < 315))) {// landscape
					mCurrentOrientation = false;
					Log.i(TAG, "横屏");
				}
			}
		};
		mOrEventListener.enable();
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.take_photo) {
			mCamera.takePicture(mShutter, null, mJpeg);
		} else {
		}
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.setDisplayOrientation(90);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		// SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览

				Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
				Size largestSize = getBestSupportedSize(parameters
						.getSupportedPreviewSizes());
				parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
				largestSize = getBestSupportedSize(parameters
						.getSupportedPictureSizes());// 设置捕捉图片尺寸
				parameters.setPictureSize(largestSize.width, largestSize.height);
				mCamera.setParameters(parameters);

				try {
					mCamera.startPreview();
				} catch (Exception e) {
					if (mCamera != null) {
						mCamera.release();
						mCamera = null;
					}
				}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	};
	@Override
	public void onResume() {
		super.onResume();
		// 开启相机
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
			// i=0 表示后置相机
		} else
			mCamera = Camera.open();
	}
	@SuppressLint("NewApi")
	@Override
	public void onPause() {
		super.onPause();
		// 释放相机
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}

	}
	private Size getBestSupportedSize(List<Size> sizes) {
		// 取能适用的最大的SIZE
		Size largestSize = sizes.get(0);
		int largestArea = sizes.get(0).height * sizes.get(0).width;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				largestArea = area;
				largestSize = s;
			}
		}
		return largestSize;
	}
}
