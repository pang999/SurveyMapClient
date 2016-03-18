package com.surveymapclient.view;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.HomeActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.entity.RectangleLineBean;
import com.surveymapclient.impl.ClipCallBack;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.model.AngleModel;
import com.surveymapclient.model.CoordinateModel;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;
import com.surveymapclient.model.RectangleModel;
import com.surveymapclient.view.StardyView.DrawPath;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Environment;
import android.os.Vibrator;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class DefineView extends View{
	
	DialogCallBack dialogCallBack;
	VibratorCallBack vibratorCallBack;
	ClipCallBack clipCallBack;
	//画点
	private Paint point;
	private Paint huabu;
	
//    private Paint   circlePaint;
//    private Path    circlePath;
    
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;// 画布的画笔
    private Bitmap  mBitmap;
    private Paint   mPaint;// 真实的画笔
    
    	
	final int width = Contants.sreenWidth*3;
	final int height = Contants.screenHeight*3;
	public int screen_x=-Contants.sreenWidth;
	public int screen_y=-Contants.screenHeight;
	
	private boolean isFirst=false;
	
	private float startDistance;

	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	private int mode = DRAG;
	// 第一个按下的手指的点   
    private PointF dragPoint = new PointF();
	private float scale=1;
	
//	public boolean isDrag=true;
//	public boolean isDan=false;
//	public boolean isDuo=false;



    //画直线
    private boolean isMovingdraw=false;
    private boolean mIsLongPressed =false;//长按事件
    private boolean mIsShortPressed=false;
    private boolean isMoveLinedrag=false;

    //连续线段获得条数
    public List<CouplePointLineBean> totallist=new ArrayList<CouplePointLineBean>();
    
    private boolean isWriteText=false;
//    private boolean isLineChange=false;
    //选中直线
	private static int m=-1;
    //路径对象 
    class DrawPath{
        Path shadowpath;
        Paint shadowpaint;
    }
    
    private LinesModel linesModel;
    private PolygonModel polygonModel;
    private RectangleModel rectangleModel;
    private CoordinateModel coordinateModel;
    private AngleModel angleModel;
    
//    float left, top, right, bottom;
    private  Path sPath = new Path();  
    private  Matrix matrix = new Matrix();  
    // 放大镜的半径  
  
    private static final int RADIUS = 80;  
    // 放大倍数  
    private static final int FACTOR = 2;  
    private int mCurrentX, mCurrentY,sCurrentX, sCurrentY;
    private int rmCurrentX,rmCurrentY;
    private Canvas clipCanvas;
    
	public DefineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
//		mPath = new Path();
		linesModel=new LinesModel();
		polygonModel=new PolygonModel();
		rectangleModel=new RectangleModel();
		coordinateModel=new CoordinateModel();
		angleModel=new AngleModel();
		huabu=ViewContans.generatePaint(Color.GRAY, (float)0.5);
		point=ViewContans.generatePaint(Color.RED, 20);
		initCanvas();
		isFirst=true;	
		dialogCallBack=(DialogCallBack) context;
		vibratorCallBack=(VibratorCallBack) context;	
//		clipCallBack=(ClipCallBack)context ;
		
		sPath.addCircle(RADIUS, RADIUS, RADIUS, Direction.CW); 
//		sPath.addRect(RADIUS, RADIUS, RADIUS, RADIUS, Direction.CW);
        matrix.setScale(FACTOR, FACTOR);  
        clipCanvas=new Canvas();
	}
	//初始化画布
    public void initCanvas(){
         
        mPaint = ViewContans.generatePaint(Color.RED, 5);  
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);    
        //画布大小 
        mBitmap = Bitmap.createBitmap(Contants.sreenWidth*3, Contants.screenHeight*3, 
            Bitmap.Config.RGB_565);
       
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中      
        mCanvas.drawColor(Color.WHITE);
        initCanvasHuabu(mCanvas);
//      mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
         
    }
	
	private void initCanvasHuabu(Canvas canvas){
		canvas.drawColor(Color.WHITE);
		final int space = 20;   //长宽间隔  
	    int vertz = 0;  
	    int hortz = 0;  
	    int xcount=height/10;
	    int ycount=width/10;
		canvas.scale(scale, scale,width/2,height/2);
		// 将前面已经画过得显示出来
	    canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //显示旧的画布       
	    polygonModel.DrawPath(canvas);
		angleModel.DrawPath(canvas);
	    for(int i=0;i<xcount/2+1;i++){  
	       canvas.drawLine(0,  vertz,  width, vertz, huabu);  
	       vertz+=space;  
	    }   
	    for (int i = 0; i < ycount/2+1; i++) {
	       canvas.drawLine(hortz, 0, hortz, height, huabu);  
	       hortz+=space;  
		}
        canvas.drawPoint(0, 0, point);
	    canvas.drawPoint( width, 0, point);
	    canvas.drawPoint(0,  height, point);
	    canvas.drawPoint( width,  height, point) ;
	    canvas.drawPoint(width/2, height/2, point);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isFirst) {
			isFirst=false;
			layout(screen_x, screen_y, 3*Contants.sreenWidth+screen_x, 3*Contants.screenHeight+screen_y);
		}		
		initCanvasHuabu(canvas);      
        if (DefineActivity.TYPE==Contants.SINGLE&&isMovingdraw) {       	
        	if (isMoveLinedrag) {
				linesModel.MoveDrawLine(canvas);
			}else {
				if (linesModel.LineType==Contants.IS_TWO_LINES) {
	        		linesModel.DrawLines(canvas);
	        	}else{
	        		linesModel.DrawLine(canvas);
	    		}
//				ClipCavas(canvas);
			}       	           
		}
        if (DefineActivity.TYPE==Contants.RECTANGLE&&isMovingdraw) {
        	if (isMoveLinedrag) {
				rectangleModel.MoveDrawRectangle(canvas);
			}else {
				rectangleModel.DrawRectangle(canvas);
			}			
		} else if (DefineActivity.TYPE==Contants.COORDINATE&&isMovingdraw) {
			if (isMoveLinedrag) {
				coordinateModel.MoveDrawCoordinate(canvas);
			}else {
				coordinateModel.DrawCoordinate(canvas);
			}
			
		}else if(DefineActivity.TYPE==Contants.ANGLE&&isMovingdraw){
			if (isMoveLinedrag) {
				angleModel.MoveDrawAngle(canvas);
			}
		}if (isWriteText) {
        	Logger.i("选中的值", "m="+m);
        	linesModel.LineChangeToText(totallist, canvas, m);
		}
        
     
	}
	
	private void ClipCavas(Canvas canvas){
		// TODO Auto-generated method stub
		// 剪切  
        Logger.i("清屏高度",""+ DefineActivity.TopHeaght);
//		canvas.translate(mCurrentX-RADIUS, mCurrentY-RADIUS ); 
//		sPath.moveTo(sCurrentY, sCurrentY);
//		sPath.quadTo(sCurrentY,sCurrentY,mCurrentX ,mCurrentY);
//		canvas.clipPath(sPath);
		
        int c=80;
//        canvas.drawRect(mCurrentX-c, mCurrentY-c, mCurrentX+c, mCurrentY+c, ViewContans.generatePaint(Color.RED, 2));
        canvas.clipRect(mCurrentX-c, mCurrentY-c, mCurrentX+c, mCurrentY+c);
        // 画放大后的图         
//        canvas.translate(RADIUS - mCurrentX * FACTOR, RADIUS - mCurrentY * FACTOR); 
//		 initCanvas();
//        canvas.drawLine((float)sCurrentX, (float)sCurrentY,(float) mCurrentX,(float)  mCurrentY, ViewContans.generatePaint(Color.RED,3));
         canvas.drawBitmap(mBitmap, matrix, null); 
//         clipCallBack.OnClipCallBack(canvas);
	}
	//长按事件参数值
	float lastx=0;
	float lasty=0;
	long lastDownTime=0;
	boolean Yes=false;	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		float rx=event.getRawX();
		float ry=event.getRawY();
		rmCurrentX=(int) rx;
		rmCurrentY=(int) ry;
		// TODO Auto-generated method stub
		switch (event.getAction()& MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: 
        	isMovingdraw=true;
        	lastx=x;
        	lasty=y;
        	lastDownTime= event.getDownTime();   
        	sCurrentX = (int) event.getX();  
            sCurrentY = (int) event.getY();  
			if (DefineActivity.TYPE==Contants.SINGLE) {							
				int i=linesModel.PitchOnLine(totallist, x, y);
				if (i>=0) {				
					isMoveLinedrag=true;
					linesModel.MoveLine_down(totallist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					linesModel.Line_touch_down(x, y);
				}				
			}else if (DefineActivity.TYPE==Contants.CONTINU) {	
				polygonModel.Continuous_touch_down(x, y);				
			}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
				int i=rectangleModel.PitchOnRectangle(rectangleModel.getRectlist, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					rectangleModel.MoveRectangle_down(rectangleModel.getRectlist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					rectangleModel.Rectangle_touch_down(x, y);
				}			
			}else if (DefineActivity.TYPE==Contants.COORDINATE) {
				int i=coordinateModel.PitchOnCoordinate(coordinateModel.getCoordlist, x, y);	
				if (i>=0) {
					isMoveLinedrag=true;
					coordinateModel.MoveCoordinate_down(coordinateModel.getCoordlist, i, rx, ry);
					DrawAllOnBitmap();
				} else {
//					coordinateModel.Coordinate_touch_down(x, y);
				}
			}else if (DefineActivity.TYPE==Contants.ANGLE) {
				int i=angleModel.PitchOnAngle(angleModel.getAnglelist, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					angleModel.MoveAngle_down(angleModel.getAnglelist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					angleModel.Angle_touch_down(x, y);	
				}
			}else if(DefineActivity.TYPE==Contants.DRAG){
				mIsShortPressed=false;
				drag_touch_start(rx, ry);
			}
    	    invalidate();
            break;
        case MotionEvent.ACTION_POINTER_DOWN: //如果有一只手指按下屏幕，后续又有一个手指按下     // 两只手指按下
        	zoom_down(event);
		    invalidate();
			break;
        case MotionEvent.ACTION_MOVE:  
        	mCurrentX = (int) event.getX();  
            mCurrentY = (int) event.getY();  
        	Logger.i("屏幕与视图", "屏幕:x="+getLeft()+",screem_x="+screen_x+",RowX="+(int)rx+"，视图:x="+(int)x+"; 屏幕:y="+getTop()+",screem_y="+screen_y+",RowY="+(int)ry+",视图:y="+(int)y);
            if (DefineActivity.TYPE==Contants.SINGLE) {       	
            	if (isMoveLinedrag) {        		
					linesModel.MoveLine_move(rx, ry);
				}else {					
	    			linesModel.Line_touch_move(x, y);
				}
			}else if (DefineActivity.TYPE==Contants.CONTINU) {
				polygonModel.Continuous_touch_move(x, y);
			}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
				if (isMoveLinedrag) {
					rectangleModel.MoveRectangle_move(rx, ry);
				}else {				
	    			rectangleModel.Rectangle_touch_move(x, y);
				}			
			}else if (DefineActivity.TYPE==Contants.COORDINATE) {
				if (isMoveLinedrag) {
					coordinateModel.MoveCoordinate_move(rx, ry);
				}else {				
	    			coordinateModel.Coordinate_touch_move(x, y);						
				}				
			}else if (DefineActivity.TYPE==Contants.ANGLE) {
				if (isMoveLinedrag) {
					angleModel.MoveAngle_move(rx, ry);
				}else {					
	    			angleModel.Angle_touch_move(x, y);
				}				
			}else if(DefineActivity.TYPE==Contants.DRAG) {				
				if (mode==DRAG) {
	            	drag_touch_move(rx, ry);
				}else if(mode==ZOOM){
					setzoomcanvas(event);
				}
			}	
            if (!mIsLongPressed) {
            	mIsLongPressed=ViewContans.isLongPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 1000);
            	Yes=true;
        	}
            if (mIsLongPressed) {        		
        		if (Yes) {
        			Yes=false;        			
        			if (angleModel.ExtandAngle(angleModel.getAnglelist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
        				DefineActivity.TYPE=Contants.ANGLE;
					}
        			if (coordinateModel.ExtandCoordinate(coordinateModel.getCoordlist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.COORDINATE;
					}
        			if (rectangleModel.ExtandRectangle(rectangleModel.getRectlist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.RECTANGLE;
					} 
        			if (linesModel.ExtendLine(totallist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.SINGLE;
					} 
        			DrawAllOnBitmap();
				}          		
        		if (DefineActivity.TYPE==Contants.SINGLE) {
					linesModel.Line_touch_move(x, y);
				}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
					rectangleModel.Rectangle_touch_move(x, y);
				}else if (DefineActivity.TYPE==Contants.COORDINATE) {
					coordinateModel.Coordinate_touch_move(x, y);
				}else if (DefineActivity.TYPE==Contants.ANGLE) {
					angleModel.Angle_touch_move(x, y);	
				}
			}
    	    invalidate();
            break;
        case MotionEvent.ACTION_UP:
        	isMovingdraw=false;
			if (DefineActivity.TYPE==Contants.CONTINU) {      		
        		polygonModel.Continuous_touch_up(x, y, mCanvas);  			
			}else if (DefineActivity.TYPE==Contants.SINGLE) {						
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					linesModel.MoveLine_up(mCanvas, rx, ry);
				}else {
					mIsLongPressed=false;				
					linesModel.Line_touch_up(x, y , mCanvas);	
				}
			}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					rectangleModel.MoveRectangle_up(mCanvas);
				} else {
					mIsLongPressed=false;
					rectangleModel.Rectangle_touch_up(x, y,mCanvas);
				}				
			}else if (DefineActivity.TYPE==Contants.COORDINATE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					coordinateModel.MoveCoordinate_up(mCanvas);
				}else {
					mIsLongPressed=false;
					coordinateModel.Coordinate_touch_up(x,y,mCanvas);
				}				
			}else if (DefineActivity.TYPE==Contants.ANGLE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					angleModel.MoveAngle_up(mCanvas);
				}else {
					mIsLongPressed=false;
					angleModel.Angle_touch_up(x, y, mCanvas);
				}				
			}else {
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
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
			if (totallist.size()>=3) {
				polygonModel.DrawShadowArea(totallist, mCanvas);
			}
			DefineActivity.TYPE=Contants.DRAG;
    	    invalidate();
            break;
        case MotionEvent.ACTION_POINTER_UP: //一只手指离开屏幕，但还有一只手指在上面会触此事件
			//什么都没做
			mode = NONE;
			break;
        }
		return true;
	}
	private void DrawAllOnBitmap(){
		initCanvas(); 
		linesModel.DrawLinesOnBitmap(totallist, mCanvas);
		rectangleModel.DrawRectanleOnBitmap(rectangleModel.getRectlist, mCanvas);
		coordinateModel.DrawCoordinateOnBitmap(coordinateModel.getCoordlist, mCanvas);
		angleModel.DrawAngleOnBitmap(angleModel.getAnglelist, mCanvas);
	}
	/**
	 * 画布拖拽
	 * @param rx
	 * @param ry
	 */
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
		    layout(screen_x, screen_y, width+screen_x, height+screen_y);
		    DefineActivity.LocationXY(screen_x, screen_y);
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
	public void SetwriteLineText(String text,int place){
		linesModel.AddTextOnLine(totallist, mCanvas, m,text,place);
		layout(screen_x, screen_y, width+screen_x, height+screen_y);
		linesModel.AddLineNameAtIndex(totallist,text,m);
	}
	public void LineChangeToText(){
		isWriteText=true;
		invalidate();
	}
	public void LineNoChangeToText(){
		isWriteText=false;
		invalidate();
	}
	/**
	 * 画布缩放
	 * @param event
	 */
	private void zoom_down(MotionEvent event){
		startDistance = ViewContans.distance(event);//记下两点的距离
		if(startDistance > 5f) {  //两个手指之间的最小距离像素大于5，认为是多点触摸
			mode = ZOOM;
		}
	}
	private void setzoomcanvas(MotionEvent event){
		float distance = ViewContans.distance(event);  //两点之间的距离
		if(distance > 5f) {
			scale = distance / startDistance;	
		}
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
	 
	 public void ChangeLineAttribute(int index,String name,double angle,int color
				,double lenght,boolean isfull,float width,String desc){
		//调用初始化画布函数以清空画布
         initCanvas(); 
         linesModel.ChangeLineAttributeAtIndex(totallist, mCanvas, index, name, angle, color, lenght, isfull, width, desc);
         invalidate();
	 }
	 public void LocationXY(){
		 screen_x=-Contants.sreenWidth;
		 screen_y=-Contants.screenHeight;
		 layout(screen_x, screen_y, width+screen_x, height+screen_y);
		 DefineActivity.LocationXY(screen_x, screen_y);
	 }
	 //保存图片
	 public void SavaBitmap(){
		 ViewContans.saveBitmap(mBitmap);
	 }	
	 //画布网格大小初始化
	 public void ZoomCanvas(float cale){
		scale=cale;
		invalidate();
	 }	  

}
