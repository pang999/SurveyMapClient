package com.surveymapclient.view;

import com.surveymapclient.impl.ClipCallBack;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class ClipCanvasView extends View implements ClipCallBack{
	Canvas clipcanvas;

	public ClipCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		clipcanvas=new Canvas();
	}

	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.clipcanvas=canvas;
	}

	@Override
	public void OnClipCallBack(byte[] bs) {
		// TODO Auto-generated method stub
		
	}
}
