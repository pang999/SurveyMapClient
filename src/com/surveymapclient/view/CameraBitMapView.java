package com.surveymapclient.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;

public class CameraBitMapView extends View {
	
	

	DialogCallBack dialogCallBack;
	VibratorCallBack vibratorCallBack;
//	
	private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;// 画布的画笔
    private Bitmap  mBitmap;
//    private Paint   mPaint;// 真实的画笔
    private Bitmap background;
    Context mContext;
    private float startDistance;
    
    public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	private int mode = DRAG;
	// 第一个按下的手指的点   
    private PointF dragPoint = new PointF();
    private float scale=1;
    public int screen_x=0;
	public int screen_y=0;
	//画直线
    private boolean isMovingdraw=false;
    private boolean mIsLongPressed =false;//长按事件
    private boolean mIsShortPressed=false;
    private boolean isMoveLinedrag=false;
	
	public boolean isDrag=true;
	public boolean isDan=false;
	public boolean isDuo=false;
	
	private LinesModel linesModel;
	private PolygonModel polygonModel;
	
	 private boolean isWriteText=false;
	 private static int m=-1;
	
	//连续线段获得条数
    List<CouplePointLineBean> totallist=new ArrayList<CouplePointLineBean>();
    
    public CameraBitMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		linesModel=new LinesModel();
		polygonModel=new PolygonModel();
		this.mContext=context;
//		initCanvas();
	}
    
	
	//初始化画布
    public void initCanvas(){
         
//        mPaint = ViewContans.generatePaint(Color.RED, 5);  
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);    
        //画布大小 
//        mBitmap = Bitmap.createBitmap(Contants.sreenWidth, Contants.screenHeight, 
//            Bitmap.Config.RGB_565);
        mBitmap = Bitmap.createBitmap(background);  
    	mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中      
        mCanvas.drawColor(Color.TRANSPARENT);
//        mPath = new Path();
            
         
    }
    public void setimagebitmap(String path){
//    	this.background=bm;
    	// 图片路径
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
    	this.background=bitmap;
	    dialogCallBack=(DialogCallBack) mContext;
		vibratorCallBack=(VibratorCallBack) mContext;
    	initCanvas();
    }
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
//		canvas.drawBitmap(background, 0, 0, mBitmapPaint);
		// 将前面已经画过得显示出来
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //显示旧的画布
		canvas.scale(scale, scale,getWidth()/2,getHeight()/2);
		polygonModel.DrawPath(canvas);
		if (isDan&&isMovingdraw) {
			if (isMoveLinedrag) {
				linesModel.MoveDrawLine(canvas);
			}else {
				if (linesModel.LineType==Contants.IS_TWO_LINES) {
	        		linesModel.DrawLines(canvas);;
	        	}else{
	        		linesModel.DrawLine(canvas);
	    		}
			}
        	
		}
		if (isWriteText) {
        	Logger.i("选中的值", "m="+m);
        	linesModel.LineChangeToText(totallist, canvas, m);
//			linesModel.AddTextOnLine(totallist, canvas, m);
		}
	}
	//长按事件参数值
	float lastx=0;
	float lasty=0;
	long lastDownTime=0;
	boolean Yes=false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x=event.getX();
		float y=event.getY();
		float rx=event.getRawX();
		float ry=event.getRawY();
		switch (event.getAction()& MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
//			Logger.i("屏幕与视图", "ACTION_DOWN");
			
        	lastx=x;
        	lasty=y;
        	lastDownTime= event.getDownTime();    
			if (isDan) {
				isMovingdraw=true;
				int i=linesModel.PitchOnLine(totallist, x, y);
				if (i>=0) {				
					isMoveLinedrag=true;
					linesModel.MoveLine_down(totallist, i, rx, ry);
					initCanvas();  
            		linesModel.DrawLinesOnBitmap(totallist, mCanvas);
				}else {
					linesModel.Line_camera_touch_down(x, y);
				}
			}else if (isDuo) {
				polygonModel.Continuous_touch_down(x, y);
			}else if (isDrag) {
				mIsShortPressed=false;
				drag_touch_start(rx, ry);
			}
			
			break;
		case MotionEvent.ACTION_POINTER_DOWN: //如果有一只手指按下屏幕，后续又有一个手指按下     // 两只手指按下
			Logger.i("中心点距离", "ACTION_POINTER_DOWN");
			zoom_down(event);
        	break;
		case MotionEvent.ACTION_MOVE:
//        	Logger.i("屏幕与视图", "屏幕:x="+getLeft()+",screem_x="+screen_x+",RowX="+(int)rx+"，视图:x="+(int)x+"; 屏幕:y="+getTop()+",screem_y="+screen_y+",RowY="+(int)ry+",视图:y="+(int)y);
			if (isDan) {
				if (isMoveLinedrag) {        		
					linesModel.MoveLine_move(rx, ry);
				}else {
					if (!mIsLongPressed) {
	                	mIsLongPressed=ViewContans.isLongPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 1000);
	                	Yes=true;
	            	}
	            	if (mIsLongPressed) {        		
	            		if (Yes) {
	            			Yes=false;        			
	            			if (linesModel.ExtendLine(totallist, x, y)) {
								vibratorCallBack.onVibratorCallBack();
							}    		
	                		initCanvas();  
	                		linesModel.DrawLinesOnBitmap(totallist, mCanvas);
						}          		
	            		linesModel.Line_touch_move(x, y);
	    			}else {
	    				linesModel.Line_touch_move(x, y);
					}
				}			
			}else if (isDuo) {
				polygonModel.Continuous_touch_move(x, y);
			}else if (isDrag) {
				if (mode==DRAG) {
	            	drag_touch_move(rx, ry);
				}else if(mode==ZOOM){
					setzoomcanvas(event);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			
			if (isDan) {			
				isMovingdraw=false;
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					linesModel.MoveLine_camera_up(mCanvas, rx, ry);
				}else {
					mIsLongPressed=false;
					linesModel.Line_camera_touch_up(x, y, mCanvas);
				}
			}else if (isDuo) {
				polygonModel.Continuous_camera_touch_up(x, y, mCanvas);
			}else {
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200) 	;												      	
	        	if (mIsShortPressed) {
	        		int m=linesModel.PitchOnLine(totallist, x, y);
	        		if (m>=0) {
	    				dialogCallBack.onDialogCallBack(totallist.get(m), m);
					}
				}
			}
			Logger.i("添加个数", "在line中 ，lines="+linesModel.list.size());  
			Logger.i("添加个数", "在polygon中 ， lines="+polygonModel.continuouslist.size());
			for (int i = 0; i < linesModel.list.size(); i++) {
	        	PointF start=linesModel.list.get(i).getStartPoint();
	        	PointF stop=linesModel.list.get(i).getStopPoint();
	        	linesModel.AddLinesParams(totallist, start, stop);	        	
			}
			linesModel.list.clear();
			for (int i = 0; i < polygonModel.continuouslist.size(); i++) {
				PointF start=polygonModel.continuouslist.get(i).getStartPoint();
	        	PointF stop=polygonModel.continuouslist.get(i).getStopPoint(); 
	        	linesModel.AddLinesParams(totallist, start, stop);
	        	
			} 
			polygonModel.continuouslist.clear();
			Logger.i("添加个数", "在total中 ， lines="+totallist.size());
			break;		
		}
		invalidate();
		return true;
	}
	private void drag_touch_start(float rx,float ry){
		dragPoint.set(rx,ry);
        mode=DRAG;
	}
	private void drag_touch_move(float rx,float ry){
		float dx=rx-dragPoint.x;
	    float dy=ry-dragPoint.y;
		if (Math.sqrt(dx*dx+dy*dy)>5f) {
			dragPoint.set(rx,ry);  				
			screen_x+=(int)dx;
			screen_y+=(int)dy;
		    layout(screen_x, screen_y, getWidth()+screen_x, getHeight()+screen_y);
		}
	}
	public boolean SetLineText(int rx,int ry){
		float gx=(float)(-screen_x+rx);
		float gy=(float)(-screen_y+ry-50);
		int i=linesModel.PitchOnLineToText(totallist, gx, gy);
		if (i>=0) {
			m=i;	
			
			return true;
		}else {
			
			return false;
		}		
	}
	public void SetwriteLineText(String text){
//		invalidate();
//		linesModel.AddTextOnLine(totallist, mCanvas, m,text);
	}
	public void LineChangeToText(){
		isWriteText=true;
//		isLineChange=true;
		invalidate();
	}
	public void LineNoChangeToText(){
//		isLineChange=true;
		isWriteText=false;
		invalidate();
	}
	/**
	 * 画布缩放
	 * @param event
	 */
	private void zoom_down(MotionEvent event){
		startDistance = distance(event);//记下两点的距离
		if(startDistance > 5f) {  //两个手指之间的最小距离像素大于5，认为是多点触摸
			mode = ZOOM;
		}
		Logger.i("中心点距离", "startDistance="+startDistance);
	}
	private void setzoomcanvas(MotionEvent event){
		float distance = distance(event);  //两点之间的距离
		Logger.i("中心点距离", "distance="+distance);
		if(distance > 5f) {
			scale = distance / startDistance;	
		}
	}
	/**计算两点之间的距离像素**/
	public float distance(MotionEvent event) {
		// TODO Auto-generated method stub
		float eX = getWidth()/2 - event.getRawX();  //后面的点坐标 - 前面点的坐标 
		float eY = getHeight()/2 - event.getRawY();
		return (float) Math.sqrt(eX * eX + eY * eY);
	}
	/**
	  * 撤销的核心思想就是将画布清空，
	  * 将保存下来的Path路径最后一个移除掉，
	  * 重新将路径画在画布上面。
	  */
	 public void UnDo(){
	     if (totallist!=null&&totallist.size()>0) {
	    	//调用初始化画布函数以清空画布
	         initCanvas();  
	         linesModel.DeleteLine(totallist, mCanvas);
	         invalidate();
		 }	 
    }
	/**
	  * 删掉某条
	  */
	 public void RemoveIndexLine(int index){
        if(totallist != null && totallist.size() > 0){
            //调用初始化画布函数以清空画布
           initCanvas();            
           linesModel.RemoveIndexLine(totallist, mCanvas, index);
           invalidate();// 刷新
        }
    }
	 //保存图片
	 public void SavaBitmap(){
		 ViewContans.saveBitmap(mBitmap);
	 }	
}
