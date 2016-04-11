package com.surveymapclient.view;

import com.surveymapclient.common.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

public class MagnifyView extends View {

	Bitmap bitmap;
	private  Matrix matrix = new Matrix(); 
	public MagnifyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		matrix.setScale(2, 2);
	}
	public void setDrawableCanvas(Bitmap bitmap){
		this.bitmap=bitmap;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (bitmap!=null) {
//			Logger.i("∑≈¥ÛView", "canvas");
//			canvas=this.mCanvas;
			canvas.drawBitmap(bitmap, matrix, null);
		}
	}

}
