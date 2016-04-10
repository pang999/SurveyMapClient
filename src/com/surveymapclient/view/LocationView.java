package com.surveymapclient.view;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.ViewContans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LocationView extends View {

	Paint paint;
	Paint point;
	float x=0,y=0;
	public LocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint=ViewContans.generatePaint(Color.RED, 1,true);
		point=ViewContans.generatePaint(Color.RED, 10,true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);		
		canvas.drawCircle(getWidth()-100, getHeight()/2, 25,paint);
		canvas.drawCircle(getWidth()-100, getHeight()/2, 5, paint);
		canvas.drawPoint(getWidth()-100+x, getHeight()/2+y, point);
	}
	public void LocationSketch(int cscreenX,int cscreenY){
		int sx=Contants.sreenWidth;
		int sy=Contants.screenHeight;
		int xfz=20*(cscreenX-sx);
		int yfz=20*(cscreenY-sy);
		this.x=(float)xfz/sx+40;
		this.y=(float)yfz/sy+40;
		invalidate();
	}
}
