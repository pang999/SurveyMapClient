package com.surveymapclient.view;

import java.util.ArrayList;
import java.util.List;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.impl.ClipCallBack;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.model.AngleModel;
import com.surveymapclient.model.CoordinateModel;
import com.surveymapclient.model.LineAndPolygonModel;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;
import com.surveymapclient.model.RectangleModel;
import com.surveymapclient.model.TextModel;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DefineView extends View{
	
	DialogCallBack dialogCallBack;
	VibratorCallBack vibratorCallBack;
	ClipCallBack clipCallBack;
	
    private Canvas  mCanvas;
    private Bitmap  mBitmap;    
    	
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

    //画直线
    private boolean isMovingdraw=false;
    private boolean mIsLongPressed =false;//长按事件
    private boolean mIsShortPressed=false;
    private boolean isMoveLinedrag=false;

    //连续线段获得条数
//    public List<LineBean> totallist=new ArrayList<LineBean>();
//    public List<PolygonBean> totalPolys=new ArrayList<PolygonBean>();
    private boolean isWriteText=false;
    //选中直线
	private static int m=-1;
    
    private LinesModel linesModel;
    private PolygonModel polygonModel;
    private LineAndPolygonModel linepoly;
    private RectangleModel rectangleModel;
    private CoordinateModel coordinateModel;
    private AngleModel angleModel;
    private TextModel textModel;
    
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
		linesModel=new LinesModel();
		polygonModel=new PolygonModel();
		rectangleModel=new RectangleModel();
		coordinateModel=new CoordinateModel();
		linepoly=new LineAndPolygonModel();
		angleModel=new AngleModel();
		textModel=new TextModel();
		initCanvas();
		isFirst=true;	
		dialogCallBack=(DialogCallBack) context;
		vibratorCallBack=(VibratorCallBack) context;			
		sPath.addCircle(RADIUS, RADIUS, RADIUS, Direction.CW); 
        matrix.setScale(FACTOR, FACTOR);  
        clipCanvas=new Canvas();
        DefineActivity.TYPE=Contants.DRAG;
	}
	//初始化画布
    public void initCanvas(){      
        //画布大小 
        mBitmap = Bitmap.createBitmap(Contants.sreenWidth*3, Contants.screenHeight*3, 
            Bitmap.Config.RGB_565);      
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中      
        ViewContans.initCanvasHuabu(mCanvas,mBitmap,scale);         
    }
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isFirst) {
			isFirst=false;
			layout(screen_x, screen_y, 3*Contants.sreenWidth+screen_x, 3*Contants.screenHeight+screen_y);
		}		
	    ViewContans.initCanvasHuabu(canvas,mBitmap,scale);
		polygonModel.DrawPath(canvas);
		angleModel.DrawPath(canvas);
        if (DefineActivity.TYPE==Contants.SINGLE&&isMovingdraw) {       	
        	if (isMoveLinedrag) {
				linesModel.MoveDrawLine(canvas);
			}else {
				if (linesModel.LineType==Contants.IS_TWO_LINES) {
	        		linesModel.DrawLines(canvas);
	        	}else{
	        		linesModel.DrawLine(canvas);
	    		}	
				ClipCavas(canvas);
			}          	
		}
        if (DefineActivity.TYPE==Contants.CONTINU&&isMovingdraw) {
        	if (isMoveLinedrag) {
        		polygonModel.MoveDrawPolygon(canvas);
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
		}else if (DefineActivity.TYPE==Contants.ANGLE_AXIS&&isMovingdraw) {
			angleModel.DrawAngle(canvas);
		}else if (DefineActivity.TYPE==Contants.TEXT) {
			textModel.DrawText(canvas);
//			DefineActivity.TYPE=Contants.DRAG;
		}else if (DefineActivity.TYPE==Contants.COORDINATE_AXIS&&isMovingdraw) {
			coordinateModel.DrawCoordinate(canvas);
		}if (isWriteText) {
        	Logger.i("选中的值", "m="+m);
        	linesModel.LineChangeToText(linesModel.Getlines, canvas, m);
		} 
		
	}
	
	private void ClipCavas(Canvas canvas){
		// TODO Auto-generated method stub
		// 剪切  
        Logger.i("清屏高度",""+ DefineActivity.TopHeaght);
		canvas.translate(mCurrentX-RADIUS, mCurrentY-RADIUS ); 
//		sPath.moveTo(sCurrentY, sCurrentY);
//		sPath.quadTo(sCurrentY,sCurrentY,mCurrentX ,mCurrentY);
		canvas.clipPath(sPath);
		
        int c=80;
        canvas.drawRect(mCurrentX-c, mCurrentY-c, mCurrentX+c, mCurrentY+c, ViewContans.generatePaint(Color.RED, 2,true));
//        canvas.clipRect(mCurrentX-c, mCurrentY-c, mCurrentX+c, mCurrentY+c);
        // 画放大后的图         
        canvas.translate(RADIUS - mCurrentX * FACTOR, RADIUS - mCurrentY * FACTOR); 
//		 initCanvas();
//        canvas.drawLine((float)sCurrentX, (float)sCurrentY,(float) mCurrentX,(float)  mCurrentY, ViewContans.generatePaint(Color.RED,3));
//        canvas.translate(Contants.sreenWidth/2-screen_x, Contants.screenHeight/2-screen_y ); 
        canvas.drawLine(sCurrentX, sCurrentY, mCurrentX, mCurrentY, ViewContans.generatePaint(Color.RED, 4,true));
        canvas.drawPoint(mCurrentX, mCurrentY, ViewContans.generatePaint(Color.RED, 20,true));
        canvas.drawBitmap(mBitmap, matrix, null); 
        Logger.i("mBitmap大小", mBitmap.toString());
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
				int i=linesModel.PitchOnLine(linesModel.Getlines, x, y);
				if (i>=0) {				
					isMoveLinedrag=true;
					linesModel.MoveLine_down(linesModel.Getlines, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					linesModel.Line_touch_down(x, y);
				}				
			}else if (DefineActivity.TYPE==Contants.CONTINU) {	
				int i=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
//				if (i>=0) {
//					isMoveLinedrag=true;
//					Logger.i("点中多边形", "i="+i);
//					polygonModel.MovePolygon_down(polygonModel.GetpolyList, i, rx, ry);
//				}else {
					polygonModel.Continuous_touch_down(x, y);
//				}								
			}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
				int i=rectangleModel.PitchOnRectangle(rectangleModel.GetRectlist, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					rectangleModel.MoveRectangle_down(rectangleModel.GetRectlist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					rectangleModel.Rectangle_touch_down(x, y);
				}			
			}else if (DefineActivity.TYPE==Contants.COORDINATE) {
				int i=coordinateModel.PitchOnCoordinate(coordinateModel.GetCoordlist, x, y);	
				if (i>=0) {
					isMoveLinedrag=true;
					coordinateModel.MoveCoordinate_down(coordinateModel.GetCoordlist, i, rx, ry);
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
			int ti=textModel.PitchOnText(textModel.getTextlist, x, y);
			if (ti>=0) {
				DefineActivity.TYPE=Contants.TEXT;
				Logger.i("获取文字", "i="+ti);
//				isMoveLinedrag=true;
				textModel.MoveText_down(textModel.getTextlist, ti, rx, ry);
				DrawAllOnBitmap();
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
//				if (isMoveLinedrag) {
//					polygonModel.MovePolygon_move(rx, ry);
//				}else {
					polygonModel.Continuous_touch_move(x, y);
//				}			
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
			}else if (DefineActivity.TYPE==Contants.TEXT) {
//				if (isMoveLinedrag) {
					textModel.MoveText_move(rx, ry);
//				}
			}else if(DefineActivity.TYPE==Contants.DRAG) {				
				if (mode==DRAG) {
	            	drag_touch_move(rx, ry);
				}else if(mode==ZOOM){
					setzoomcanvas(event);
				}
			}	
            if (!mIsLongPressed) {
            	mIsLongPressed=ViewContans.isLongPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 750);
            	Yes=true;
        	}
            if (mIsLongPressed) {        		
        		if (Yes) {
        			Yes=false;        			
        			if (angleModel.ExtandAngle(angleModel.getAnglelist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
        				DefineActivity.TYPE=Contants.ANGLE_AXIS;
        				DrawAllOnBitmap();
					}
        			if (coordinateModel.ExtandCoordinate(coordinateModel.GetCoordlist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.COORDINATE_AXIS;
						DrawAllOnBitmap();
					}
        			if (rectangleModel.ExtandRectangle(rectangleModel.GetRectlist, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.RECTANGLE;
						DrawAllOnBitmap();
					} 
        			if (linesModel.ExtendLine(linesModel.Getlines, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.SINGLE;
						DrawAllOnBitmap();
					} 
        			Logger.i("长按时间了", "长按时间了");      			
				}          		
        		if (DefineActivity.TYPE==Contants.SINGLE) {
					linesModel.Line_touch_move(x, y);
				}else if (DefineActivity.TYPE==Contants.RECTANGLE) {
					rectangleModel.Rectangle_touch_move(x, y);
				}else if (DefineActivity.TYPE==Contants.COORDINATE_AXIS) {
					coordinateModel.ExtandCoordinateOneAxis_touch_move(x,y);
				}else if (DefineActivity.TYPE==Contants.ANGLE_AXIS) {
					angleModel.ExtandAngleBorder_touch_move(x, y);	
				}
			}
    	    invalidate();
            break;
        case MotionEvent.ACTION_UP:
        	isMovingdraw=false;
        	mIsLongPressed=false;
			if (DefineActivity.TYPE==Contants.CONTINU) {  
//				if (isMoveLinedrag) {
//					isMoveLinedrag=false;
//					polygonModel.MovePolygon_up(mCanvas);
//				}else {
	        		polygonModel.Continuous_touch_up(x, y, mCanvas);		
//				}
        		
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
			}else if (DefineActivity.TYPE==Contants.ANGLE_AXIS) {
				angleModel.ExtandAngle_touch_up(mCanvas);
			}else if (DefineActivity.TYPE==Contants.TEXT) {
//				if (isMoveLinedrag) {
//					isMoveLinedrag=false;
					textModel.MoveText_up(mCanvas);
//				}
			}else if (DefineActivity.TYPE==Contants.COORDINATE_AXIS) {
				mIsLongPressed=false;
				coordinateModel.Coordinate_touch_up(x,y,mCanvas);
			}else {
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
				if (mIsShortPressed) {
	        		int l=linesModel.PitchOnLine(linesModel.Getlines, x, y);
	        		if (l>=0) {
	    				dialogCallBack.onDialogCallBack(linesModel.Getlines.get(l), l);
					}
	        		int p=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
	        		if (p>=0) {
	        			dialogCallBack.onDialogCallBack(polygonModel.GetpolyList.get(p), p);
	        			Logger.i("选中多边形", "p="+p);
	        		}
	        		int r=rectangleModel.PitchOnRectangle(rectangleModel.GetRectlist, x, y);
	        		if (r>=0) {
						dialogCallBack.onDialogCallBack(rectangleModel.GetRectlist.get(r), r);
					}
	        		int c=coordinateModel.PitchOnCoordinate(coordinateModel.GetCoordlist, x, y);
	        		if (c>=0) {
						dialogCallBack.onDialogCallBack(coordinateModel.GetCoordlist.get(c), c);
					}
	        		int a=angleModel.PitchOnAngle(angleModel.getAnglelist, x, y);
	        		if (a>=0) {
						dialogCallBack.onDialogCallBack(angleModel.getAnglelist.get(a), a);
					}   
				}
			}
			Logger.i("添加个数", "在line中 ，lines="+linesModel.Getlines.size());  
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
		linesModel.DrawLinesOnBitmap(linesModel.Getlines, mCanvas);
		rectangleModel.DrawRectanleOnBitmap(rectangleModel.GetRectlist, mCanvas);
		coordinateModel.DrawCoordinatesOnBitmap(coordinateModel.GetCoordlist, mCanvas);
		angleModel.DrawAngleOnBitmap(angleModel.getAnglelist, mCanvas);
		textModel.DrawTextList(textModel.getTextlist, mCanvas);
		polygonModel.DrawPolygonsOnBitmap(polygonModel.GetpolyList,mCanvas);
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
	/**
	 * 添加文字到直线中
	 * @param rx
	 * @param ry
	 * @return
	 */
	public boolean SetLineText(int rx,int ry){
		float gx=(float)(-screen_x+rx);
		float gy=(float)(-screen_y+ry-50);
		int i=linesModel.PitchOnLineToText(linesModel.Getlines, gx, gy);
		if (i>=0) {
			m=i;			
			return true;
		}else {		
			return false;
		}		
	}
	public void setManyTextOnView(){
		Logger.i("文字位置", "x="+(Contants.sreenWidth/2-screen_x)+"y="+(Contants.screenHeight/2-screen_y));
		textModel.Text_touch_up(mCanvas, (Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y));
		invalidate();
	}
	public void SetwriteLineText(String text){
		linesModel.AddTextOnLine(linesModel.Getlines, mCanvas, m,text,10);		
	}
	public void LineChangeToText(){
		isWriteText=true;
		invalidate();
	}
	public void LineNoChangeToText(){
		isWriteText=false;
		invalidate();
	}
	public List<LineBean> BackLinelist(){
		return linesModel.Getlines;
	}
	public List<RectangleBean> BackRectlist(){
		return rectangleModel.GetRectlist;
	}
	public List<PolygonBean> BackPolylist(){
		return polygonModel.GetpolyList;
	}
	public List<CoordinateBean> BackCoorlist(){
		return coordinateModel.GetCoordlist;
	}
	public List<AngleBean> BackAnglelist(){
		return angleModel.getAnglelist;
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
	     if (linesModel.Getlines!=null&&linesModel.Getlines.size()>0) {
	    	//调用初始化画布函数以清空画布
	         initCanvas();  
	         linesModel.DeleteLine(linesModel.Getlines, mCanvas);
	         invalidate();
		 }	 
     }
	 /**
	  * 删掉某条
	  */
	 public void RemoveIndexLine(int index){
         if(linesModel.Getlines != null && linesModel.Getlines.size() > 0){
            //调用初始化画布函数以清空画布
        	linesModel.Getlines.remove(index);
            DrawAllOnBitmap();                       
            invalidate();// 刷新
         }
     }
	 public void RemoveIndexRectangle(int index){
		 if (rectangleModel.GetRectlist!=null&&rectangleModel.GetRectlist.size()>0) {
			rectangleModel.GetRectlist.remove(index); 
			DrawAllOnBitmap();  
			invalidate();
		}
	 }
	 public void RemoveIndexCoordinate(int index){
		 if (coordinateModel.GetCoordlist!=null&&coordinateModel.GetCoordlist.size()>0) {
			coordinateModel.GetCoordlist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void ChangeLineAttribute(int index,LineBean line){
		//调用初始化画布函数以清空画布
         DrawAllOnBitmap();
         linesModel.ChangeLineAttributeAtIndex(linesModel.Getlines, mCanvas, index, line);         
         invalidate();
	 }
	 public void ChangeRectangleAttribute(int index,RectangleBean rectangleBean){
		 DrawAllOnBitmap();
		 rectangleModel.ChangeRectAttributeAtIndex(rectangleModel.GetRectlist, index, mCanvas, rectangleBean);
		 invalidate();
	 }
	 public void ChangeCoordinateAttribute(int index,CoordinateBean coordinateBean){
		 DrawAllOnBitmap();
		 coordinateModel.ChangeCoorAttributeAtIndex(coordinateModel.GetCoordlist, index,
				 mCanvas, coordinateBean);
		 invalidate();
	 }
	 public void ChangeAngleAttribute(int index,AngleBean angleBean){
		 DrawAllOnBitmap();
		 angleModel.ChangeAngleBeanAttributeAtIndex(angleModel.getAnglelist, index, mCanvas, angleBean);
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
