package com.surveymapclient.view;

import com.surveymapclient.common.ViewContans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StoreDataView extends Button{
	
	private static final int WIDTH = 70;  
    private Rect rect = new Rect(0, 0, WIDTH+20, WIDTH);//绘制矩形的区域
    private int deltaX,deltaY;//点击位置和图形边界的偏移量
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
		canvas.drawText("2.989m", 10+deltaX,24+deltaX, textpaint);
		canvas.drawText("2.989m", 10+deltaX,44+deltaX, textpaint);
		canvas.drawText("2.989m", 10+deltaX,64+deltaX, textpaint);
		canvas.drawRect(deltaX, deltaX, WIDTH+20+deltaX, WIDTH+deltaX, ViewContans.generatePaint(Color.BLUE, 4, true));	
	}


}
