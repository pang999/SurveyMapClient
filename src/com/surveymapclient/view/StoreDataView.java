package com.surveymapclient.view;

import com.surveymapclient.common.ViewContans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class StoreDataView extends View{
	
	Paint textpaint;  
	public StoreDataView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	
	public StoreDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		textpaint = new Paint();
		textpaint.setColor(Color.BLUE);
		textpaint.setTextSize(20);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);	
		canvas.drawRect(10, 10, 100, getHeight()-10, ViewContans.generatePaint(Color.BLUE, 4, true));
		canvas.drawText("2.989m", 20, getHeight()/2, textpaint);
	}

}
