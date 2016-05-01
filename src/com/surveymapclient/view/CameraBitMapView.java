package com.surveymapclient.view;

import java.util.List;
import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.model.AngleModel;
import com.surveymapclient.model.AudioModel;
import com.surveymapclient.model.CoordinateModel;
import com.surveymapclient.model.LineAndPolygonModel;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;
import com.surveymapclient.model.RectangleModel;
import com.surveymapclient.model.TextModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class CameraBitMapView extends View {
	
	

	DialogCallBack dialogCallBack;
	VibratorCallBack vibratorCallBack;
//	
	private Canvas  mCanvas;
    private Paint   mBitmapPaint;// �����Ļ���
    private Bitmap  mBitmap;
//    private Paint   mPaint;// ��ʵ�Ļ���
    private Bitmap background;
    Context mContext;
    private float startDistance;
    
    public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	private int mode = DRAG;
	// ��һ�����µ���ָ�ĵ�   
    private PointF dragPoint = new PointF();
    private float scale=1;
    public int screen_x=0;
	public int screen_y=0;
	//��ֱ��
    private boolean isMovingdraw=false;
    private boolean mIsLongPressed =false;//�����¼�
    private boolean mIsShortPressed=false;
    private boolean isMoveLinedrag=false;
	
	private LinesModel linesModel;
	private PolygonModel polygonModel;
    private LineAndPolygonModel linepoly;
	private RectangleModel rectangleModel;
    private CoordinateModel coordinateModel;
    private AngleModel angleModel;
    private TextModel textModel;
    private AudioModel audioModel;
	
	 private boolean isWriteText=false;
	 private static int m=-1;
   
    public CameraBitMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		linesModel=new LinesModel();
		polygonModel=new PolygonModel();
		rectangleModel=new RectangleModel();
		coordinateModel=new CoordinateModel();
		linepoly=new LineAndPolygonModel();
		textModel=new TextModel();
		angleModel=new AngleModel();
		audioModel=new AudioModel(context);
		this.mContext=context;
		CameraActivity.TYPE=Contants.DRAG;
//		initCanvas();
	}
    
	
	//��ʼ������
    public void initCanvas(){
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);    
        mBitmap = Bitmap.createBitmap(background);  
    	mCanvas = new Canvas(mBitmap);  //����mCanvas���Ķ���������������mBitmap��      
        mCanvas.drawColor(Color.TRANSPARENT);
//      mPath = new Path();         
    }
    public void setimagebitmap(String path){
//    	this.background=bm;
    	// ͼƬ·��
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		float destWidth = dm.widthPixels;  
		float destHeight= dm.heightPixels ; 
		// ��ȡ����ͼƬ�ߴ�
		BitmapFactory.Options options=new BitmapFactory.Options();
		// ����Ϊtrue��options��Ȼ��Ӧ��ͼƬ��������������Ϊ��ͼƬ�����ڴ�
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSampleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) { // ��ͼƬ���������Ļ����ʱ
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
		// ��ǰ���Ѿ���������ʾ����
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //��ʾ�ɵĻ���
		canvas.scale(scale, scale,getWidth()/2,getHeight()/2);
		polygonModel.DrawPath(canvas);
		angleModel.DrawPath(canvas);
		if (CameraActivity.TYPE==Contants.SINGLE&&isMovingdraw) {
			
			if (isMoveLinedrag) {
				linesModel.MoveDrawLine(canvas);
			}else {
				if (linesModel.LineType==Contants.IS_TWO_LINES) {
	        		linesModel.DrawLines(canvas);
	        	}else{
	        		linesModel.DrawLine(canvas);
	    		}
			}
		}if (CameraActivity.TYPE==Contants.CONTINU&&isMovingdraw) {
        	if (isMoveLinedrag) {
        		polygonModel.MoveDrawPolygon(canvas);
			}			
		}else if (CameraActivity.TYPE==Contants.CONTINU_XETEND&&isMovingdraw) {
			polygonModel.ExtendDrawPolygon(canvas);
		}else if (CameraActivity.TYPE==Contants.RECTANGLE&&isMovingdraw) {
			if (isMoveLinedrag) {
				rectangleModel.MoveDrawRectangle(canvas);
			}else {
				rectangleModel.DrawRectangle(canvas);
			}
		} else if (CameraActivity.TYPE==Contants.COORDINATE&&isMovingdraw) {
			if (isMoveLinedrag) {
				coordinateModel.MoveDrawCoordinate(canvas);
			}else {
				coordinateModel.DrawCoordinate(canvas);
			}
			
		}else if(CameraActivity.TYPE==Contants.ANGLE&&isMovingdraw){
			if (isMoveLinedrag) {
				angleModel.MoveDrawAngle(canvas);
			}
		}else if (CameraActivity.TYPE==Contants.ANGLE_AXIS&&isMovingdraw) {
			angleModel.DrawAngle(canvas);
		}else if (CameraActivity.TYPE==Contants.TEXT) {
			textModel.DrawText(canvas);
//			CameraActivity.TYPE=Contants.DRAG;
		}else if (CameraActivity.TYPE==Contants.AUDIO) {
			audioModel.DrawMoveAudio(canvas);
		}else if (CameraActivity.TYPE==Contants.COORDINATE_AXIS&&isMovingdraw) {
			coordinateModel.DrawCoordinate(canvas);
		}
		if (isWriteText) {
        	Logger.i("ѡ�е�ֵ", "m="+m);
        	linesModel.LineChangeToText(linesModel.Getlines, canvas, m);
		}
	}
	//�����¼�����ֵ
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
//			Logger.i("��Ļ����ͼ", "ACTION_DOWN");
			isMovingdraw=true;
        	lastx=x;
        	lasty=y;
        	lastDownTime= event.getDownTime();    
			if (CameraActivity.TYPE==Contants.SINGLE) {				
				int i=linesModel.PitchOnLine(linesModel.Getlines, x, y);
				if (i>=0) {				
					isMoveLinedrag=true;
					linesModel.MoveLine_down(linesModel.Getlines, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					linesModel.Line_camera_touch_down(x, y);
				}
			}else if (CameraActivity.TYPE==Contants.CONTINU) {	
				int i=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					Logger.i("���ж����", "i="+i);
					polygonModel.MovePolygon_down(polygonModel.GetpolyList, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					polygonModel.Continuous_touch_down(x, y);
				}								
			}else if (CameraActivity.TYPE==Contants.RECTANGLE) {
				int i=rectangleModel.PitchOnRectangle(rectangleModel.GetRectlist, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					rectangleModel.MoveRectangle_down(rectangleModel.GetRectlist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					rectangleModel.Rectangle_camera_touch_down(x, y);
				}
			}else if (CameraActivity.TYPE==Contants.COORDINATE) {
				int i=coordinateModel.PitchOnCoordinate(coordinateModel.GetCoordlist, x, y);	
				if (i>=0) {
					isMoveLinedrag=true;
					coordinateModel.MoveCoordinate_down(coordinateModel.GetCoordlist, i, rx, ry);
					DrawAllOnBitmap();
				} else {
//					coordinateModel.Coordinate_touch_down(x, y);
				}
			}else if (CameraActivity.TYPE==Contants.ANGLE) {
				int i=angleModel.PitchOnAngle(angleModel.getAnglelist, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					angleModel.MoveAngle_down(angleModel.getAnglelist, i, rx, ry);
					DrawAllOnBitmap();
				}else {
					angleModel.Angle_touch_down(x, y);	
				}
			}else  if (CameraActivity.TYPE==Contants.DRAG){
				mIsShortPressed=false;
				drag_touch_start(rx, ry);
			}
			int ti=textModel.PitchOnText(textModel.GetTextlist, x, y);
			if (ti>=0) {
				CameraActivity.TYPE=Contants.TEXT;
				Logger.i("��ȡ����", "i="+ti);
//				isMoveLinedrag=true;
				textModel.MoveText_down(textModel.GetTextlist, ti, rx, ry);
				DrawAllOnBitmap();
			}
			int au=audioModel.PitchOnAudio(audioModel.GetAudiolist, x, y);
			if (au>=0) {
				CameraActivity.TYPE=Contants.AUDIO;
				audioModel.MoveAudio_dowm(audioModel.GetAudiolist, au, rx, ry);
				DrawAllOnBitmap();
			}
			invalidate();
			break;
		case MotionEvent.ACTION_POINTER_DOWN: //�����һֻ��ָ������Ļ����������һ����ָ����     // ��ֻ��ָ����
			Logger.i("���ĵ����", "ACTION_POINTER_DOWN");
			zoom_down(event);
        	break;
		case MotionEvent.ACTION_MOVE: 
//        	Logger.i("��Ļ����ͼ", "��Ļ:x="+getLeft()+",screem_x="+screen_x+",RowX="+(int)rx+"����ͼ:x="+(int)x+"; ��Ļ:y="+getTop()+",screem_y="+screen_y+",RowY="+(int)ry+",��ͼ:y="+(int)y);
			if (CameraActivity.TYPE==Contants.SINGLE) {
				if (isMoveLinedrag) {        		
					linesModel.MoveLine_move(rx, ry);
				}else {
	    			linesModel.Line_touch_move(x, y);
				}			
			}else if (CameraActivity.TYPE==Contants.CONTINU) {
				if (isMoveLinedrag) {
					polygonModel.MovePolygon_move(rx, ry);
				}else {
					polygonModel.Continuous_touch_move(x, y);
				}			
			}else if (CameraActivity.TYPE==Contants.RECTANGLE) {
				if (isMoveLinedrag) {
					rectangleModel.MoveRectangle_move(rx, ry);
				}else {
	    			rectangleModel.Rectangle_touch_move(x, y);
				}			
			}else if (CameraActivity.TYPE==Contants.COORDINATE) {
				if (isMoveLinedrag) {
					coordinateModel.MoveCoordinate_move(rx, ry);
				}else {
	    			coordinateModel.Coordinate_touch_move(x, y);						
				}
			}else if (CameraActivity.TYPE==Contants.ANGLE) {
				if (isMoveLinedrag) {
					angleModel.MoveAngle_move(rx, ry);
				}else {
	    			angleModel.Angle_touch_move(x, y);
				}
			}else if (CameraActivity.TYPE==Contants.TEXT) {
//				if (isMoveLinedrag) {
					textModel.MoveText_move(rx, ry);
//				}
			}else if (CameraActivity.TYPE==Contants.AUDIO) {
				audioModel.MoveAudio_move(rx, ry);
			}else  if (CameraActivity.TYPE==Contants.DRAG){
				if (mode==DRAG) {
	            	drag_touch_move(rx, ry);
				}/*else if(mode==ZOOM){
					setzoomcanvas(event);
				}*/
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
        				CameraActivity.TYPE=Contants.ANGLE_AXIS;
        				DrawAllOnBitmap();
					}
        			if (coordinateModel.ExtandCoordinate(coordinateModel.GetCoordlist, x, y)) {
        				vibratorCallBack.onVibratorCallBack();
						CameraActivity.TYPE=Contants.COORDINATE_AXIS;
						DrawAllOnBitmap();
					}
        			if (rectangleModel.ExtandRectangle(rectangleModel.GetRectlist, x, y)) {
        				vibratorCallBack.onVibratorCallBack();
						CameraActivity.TYPE=Contants.RECTANGLE;
						DrawAllOnBitmap();
					} 
        			if (linesModel.ExtendLine(linesModel.Getlines, x, y)) {
        				vibratorCallBack.onVibratorCallBack();
						CameraActivity.TYPE=Contants.SINGLE;
						DrawAllOnBitmap();
					}
        			if (polygonModel.ExtendPolygon(polygonModel.GetpolyList, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						CameraActivity.TYPE=Contants.CONTINU_XETEND;
						DrawAllOnBitmap();
					}
				}          		
        		if (CameraActivity.TYPE==Contants.SINGLE) {
					linesModel.Line_touch_move(x, y);
				}else if (CameraActivity.TYPE==Contants.RECTANGLE) {
					rectangleModel.Rectangle_touch_move(x, y);
				}else if (CameraActivity.TYPE==Contants.COORDINATE_AXIS) {
					coordinateModel.ExtandCoordinateOneAxis_touch_move(x,y);
				}else if (CameraActivity.TYPE==Contants.ANGLE_AXIS) {
					angleModel.ExtandAngleBorder_touch_move(x, y);	
				}else if (CameraActivity.TYPE==Contants.CONTINU_XETEND) {
					polygonModel.ExtendPolygonLine(x, y);
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:		
			isMovingdraw=false;
			mIsLongPressed=false;
			if (CameraActivity.TYPE==Contants.CONTINU) {  
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					polygonModel.MovePolygon_up(mCanvas);
				}else {
	        		polygonModel.Continuous_touch_up(x, y, mCanvas);		
				}
        		
			}else if (CameraActivity.TYPE==Contants.CONTINU_XETEND) {
				polygonModel.MovePolygon_up(mCanvas);
			}else if (CameraActivity.TYPE==Contants.SINGLE) {						
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					linesModel.MoveLine_camera_up(mCanvas);
				}else {
					mIsLongPressed=false;				
					linesModel.Line_touch_up(x, y , mCanvas);	
				}
				if (linesModel.Getlines.size()>=3) {					
					linepoly.CalculatePolyFromlist(linesModel.Getlines);
					for (int i = 0; i < linepoly.GetPolyInt.size(); i++) {
						Logger.i("ֱ��ʣ������", "GetPolyInt="+linepoly.GetPolyInt.get(i).intValue());
						linesModel.Getlines.remove(linepoly.GetPolyInt.get(i).intValue());
					}
					for (int i = 0; i < linepoly.backpolylist.size(); i++) {
						polygonModel.GetpolyList.add(linepoly.backpolylist.get(i));
						DrawAllOnBitmap();
					}
					linepoly.ClearInt();					
				}	
								
			}else if (CameraActivity.TYPE==Contants.CONTINU_XETEND) {
				polygonModel.MovePolygon_up(mCanvas);
			}else if (CameraActivity.TYPE==Contants.RECTANGLE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					rectangleModel.MoveRectangle_camera_up(mCanvas);
				} else {
					mIsLongPressed=false;
					rectangleModel.Rectangle_camera_touch_up(x, y,mCanvas);
				}
			}else if (CameraActivity.TYPE==Contants.COORDINATE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					coordinateModel.MoveCoordinate_up(mCanvas);
				}else {
					mIsLongPressed=false;
					coordinateModel.Coordinate_touch_up(x,y,mCanvas);
				}	
			}else if (CameraActivity.TYPE==Contants.ANGLE) {
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					angleModel.MoveAngle_camera_up(mCanvas);
				}else {
					mIsLongPressed=false;
					angleModel.Angle_touch_up(x, y, mCanvas);
				}
			}else if (CameraActivity.TYPE==Contants.ANGLE_AXIS) {
				angleModel.ExtandAngle_touch_up(mCanvas);
			}else if (CameraActivity.TYPE==Contants.TEXT) {
//				if (isMoveLinedrag) {
//					isMoveLinedrag=false;
					textModel.MoveText_up(mCanvas);
					mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
					if (mIsShortPressed) {
						int tx=textModel.PitchOnText(textModel.GetTextlist, x, y);
		        		if (tx>=0) {
		        			Logger.i("ѡ������", "t="+tx);
							dialogCallBack.onDialogCallBack(textModel.GetTextlist.get(tx), tx);
						}
					}
//				}
			}else if (CameraActivity.TYPE==Contants.AUDIO) {
				audioModel.MoveAudio_up(mCanvas);
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
				if (mIsShortPressed) {
					int aud=audioModel.PitchOnAudio(audioModel.GetAudiolist, x, y);
					if (aud>=0) {
						dialogCallBack.onDialogCallBack(audioModel.GetAudiolist.get(aud), aud);
					}
				}
			}else if (CameraActivity.TYPE==Contants.COORDINATE_AXIS) {
				mIsLongPressed=false;
				coordinateModel.Coordinate_touch_up(x,y,mCanvas);
			}else {
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200) 	;												      	
	        	if (mIsShortPressed) {
	        		int l=linesModel.PitchOnLine(linesModel.Getlines, x, y);
	        		if (l>=0) {
	    				dialogCallBack.onDialogCallBack(linesModel.Getlines.get(l), l,0);
					}
	        		int p=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
	        		if (p>=0) {
	        			dialogCallBack.onDialogCallBack(polygonModel.GetpolyList.get(p), p);
	        			Logger.i("ѡ�ж����", "p="+p);
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
			typeListener.onTypeChange();
			CameraActivity.TYPE=Contants.DRAG;
			break;		
		}
//		invalidate();
		return true;
	}
	private void DrawAllOnBitmap(){
		initCanvas(); 
		linesModel.DrawLinesOnBitmap(linesModel.Getlines, mCanvas);
		rectangleModel.DrawRectanleOnBitmap(rectangleModel.GetRectlist, mCanvas);
		coordinateModel.DrawCoordinatesOnBitmap(coordinateModel.GetCoordlist, mCanvas);
		angleModel.DrawAngleOnBitmap(angleModel.getAnglelist, mCanvas);
		textModel.DrawTextOnBitmap(textModel.GetTextlist, mCanvas);
		polygonModel.DrawPolygonsOnBitmap(polygonModel.GetpolyList,mCanvas);
		audioModel.DrawAudiosOnBitmap(audioModel.GetAudiolist, mCanvas);
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
		int i=linesModel.PitchOnLineToText(linesModel.Getlines, gx, gy);
		if (i>=0) {
			m=i;				
			return true;
		}		
		return false;
	}
	public void setManyTextOnView(){
		Logger.i("����λ��", "x="+(Contants.sreenWidth/2-screen_x)+"y="+(Contants.screenHeight/2-screen_y));
		textModel.Text_touch_up(mCanvas, (Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y));
	}
	public void setAudioOnView(String url,int len){
		audioModel.Audio_touch_up(mCanvas, (Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y), url,len);
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
	public List<TextBean> BackTextlist(){
		return textModel.GetTextlist;
	}
	public List<AudioBean> BackAudiolist(){
		return audioModel.GetAudiolist;
	}
	public void AddAllDataFromActivity(
			List<LineBean> linelist,
			List<PolygonBean> polygonlist,
			List<RectangleBean> rectlist,
			List<AngleBean> anglelist,
			List<CoordinateBean> coorlist,
			List<TextBean> textlist,
			List<AudioBean> audiolist){
		linesModel.Getlines=linelist;
		polygonModel.GetpolyList=polygonlist;
		rectangleModel.GetRectlist=rectlist;
		coordinateModel.GetCoordlist=coorlist;
		angleModel.getAnglelist=anglelist;
		textModel.GetTextlist=textlist;
		audioModel.GetAudiolist=audiolist;
		DrawAllOnBitmap();
	}
	/**
	 * ��������
	 * @param event
	 */
	private void zoom_down(MotionEvent event){
		startDistance = distance(event);//��������ľ���
		if(startDistance > 5f) {  //������ָ֮�����С�������ش���5����Ϊ�Ƕ�㴥��
			mode = ZOOM;
		}
		Logger.i("���ĵ����", "startDistance="+startDistance);
	}
	private void setzoomcanvas(MotionEvent event){
		float distance = distance(event);  //����֮��ľ���
		Logger.i("���ĵ����", "distance="+distance);
		if(distance > 5f) {
			scale = distance / startDistance;	
		}
	}
	/**��������֮��ľ�������**/
	public float distance(MotionEvent event) {
		// TODO Auto-generated method stub
		float eX = getWidth()/2 - event.getRawX();  //����ĵ����� - ǰ�������� 
		float eY = getHeight()/2 - event.getRawY();
		return (float) Math.sqrt(eX * eX + eY * eY);
	}
	/**
	  * �����ĺ���˼����ǽ�������գ�
	  * ������������Path·�����һ���Ƴ�����
	  * ���½�·�����ڻ������档
	  */
	 public void UnDo(){
	     if (linesModel.Getlines!=null&&linesModel.Getlines.size()>0) {
	    	//���ó�ʼ��������������ջ���
	         initCanvas();  
	         linesModel.DeleteLine(linesModel.Getlines, mCanvas);
	         invalidate();
		 }	 
    }
	 /**
	  * ɾ��ĳ��
	  */
	 public void RemoveIndexLine(int index){
         if(linesModel.Getlines != null && linesModel.Getlines.size() > 0){
            //���ó�ʼ��������������ջ���
        	linesModel.Getlines.remove(index);
            DrawAllOnBitmap();                       
            invalidate();// ˢ��
         }
     }
	 public void RemoveIndexPolygon(int index){
		 if (polygonModel.GetpolyList!=null&&polygonModel.GetpolyList.size()>0) {
				polygonModel.GetpolyList.remove(index);
				DrawAllOnBitmap();
				invalidate();
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
	 public void RemoveIndexAngle(int index){
		 if (angleModel.getAnglelist!=null&&angleModel.getAnglelist.size()>0) {
			angleModel.getAnglelist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void RemoveIndexText(int index){
		 if (textModel.GetTextlist!=null&&textModel.GetTextlist.size()>0) {
			textModel.GetTextlist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void RemoveIndexAudio(int index){
		 if (audioModel.GetAudiolist!=null&&audioModel.GetAudiolist.size()>0) {
			audioModel.GetAudiolist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void ChangeLineAttribute(int index,LineBean line){
			//���ó�ʼ��������������ջ���
       
         linesModel.ChangeLineAttributeAtIndex(linesModel.Getlines, index, line);         
         DrawAllOnBitmap();
	 }
	 public void ChangeRectangleAttribute(int index,RectangleBean rectangleBean){
		 rectangleModel.ChangeRectAttributeAtIndex(rectangleModel.GetRectlist, index, mCanvas, rectangleBean);
		  DrawAllOnBitmap();
	 }
	 public void ChangeCoordinateAttribute(int index,CoordinateBean coordinateBean){
		 coordinateModel.ChangeCoorAttributeAtIndex(coordinateModel.GetCoordlist, index,
				 mCanvas, coordinateBean);
		  DrawAllOnBitmap();
	 }
	 public void ChangeAngleAttribute(int index,AngleBean angleBean){
		 angleModel.ChangeAngleBeanAttributeAtIndex(angleModel.getAnglelist, index, mCanvas, angleBean);
		  DrawAllOnBitmap();
	 }
	 public void ChangeTextContent(int index,String text){		 
		 textModel.ChangeTextContentAtIndex(textModel.GetTextlist,index,mCanvas,text);
		 DrawAllOnBitmap();
	 }
	
	 //����ͼƬ
	 public void SavaBitmap(){
		 ViewContans.saveBitmap(mBitmap);
	 }	
	 public interface TypeChangeListener{
		 public void onTypeChange();
	 }
	 
	 private TypeChangeListener typeListener;
	 public void setOnTypeChangeListener(TypeChangeListener listener){
		 this.typeListener=listener;
	 }
}
