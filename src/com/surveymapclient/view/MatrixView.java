package com.surveymapclient.view;

import com.surveymapclient.common.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MatrixView extends ImageView {

	//����
		private Paint paint;
		private Paint zuotu;
		private Paint huabu;
		//����
		private Paint point;
		//·��
		private Path path=new Path();
			
		 /** ��������ģʽ */
	    private static final int MODE_DRAG = 1;
	    /** �Ŵ���С����ģʽ */
	    private static final int MODE_ZOOM = 2;
	    /** ��ͼģʽ*/
	    private static final int MODE_NONE = 3;
	    
	    private static int mMode=0;
	    
	    private boolean isCenter=false;
	    
	    /**  ģ��Matrix�����Գ�ʼ�� */ 
	    Matrix currentMatric=null;		
	    
	    Canvas canvas;

		// ��һ�����µ���ָ�ĵ�   
	    private PointF startPoint = new PointF();  
	    
	    private Paint generatePaint(int color,int width){
	    		paint=new Paint();
	    		paint.setColor(color);
	    		paint.setStrokeJoin(Paint.Join.ROUND);      
	    		paint.setStrokeCap(Paint.Cap.ROUND); 
	    		paint.setStrokeWidth(width); 
	    		paint.setAntiAlias(true);
	    		paint.setStyle(Style.STROKE);
	    	return paint;
	    }
	    
	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		zuotu=generatePaint(Color.RED, 3);
		point=generatePaint(Color.RED, 20);
		huabu=generatePaint(Color.GRAY, 2);	
		currentMatric=new Matrix();
		canvas=new Canvas();
//		DrawBackground(canvas);
	}
	private void DrawBackground(Canvas canvas){	
		canvas.drawColor(Color.WHITE);	
	    final int width = getWidth();
		final int height = getHeight();
		Logger.i("��Ļ", "���="+width+"�߶�="+height);
	    canvas.save();
	    final int space = 20;   //������  
	    int vertz = -3*height/2;  
	    int hortz = -3*width/2;  
	    int xcount=3*height/20;
	    int ycount=3*width/20;
	    canvas.drawPoint(-3*width/2, -3*height/2, point);
	    canvas.drawPoint( 3*width/2, -3*height/2, point);
	    canvas.drawPoint(-3*width/2,  3*height/2, point);
	    canvas.drawPoint( 3*width/2,  3*height/2, point);
	    
	    for(int i=0;i<xcount+1;i++){  
	       canvas.drawLine(-3*width/2,  vertz,  3*width/2, vertz, huabu);  
	       vertz+=space;  
	    }   
	    for (int i = 0; i < ycount+1; i++) {
	       canvas.drawLine(hortz, -3*height/2, hortz, 3*height/2, huabu);  
	       hortz+=space;  
		}
	    canvas.save();
	    canvas.drawPoint(0, 0, point);
	    canvas.restore();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		DrawBackground(canvas);
	}

}
